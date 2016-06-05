/*
 *
 *  Copyright 2016 __0x277F <0x277F@gmail.com>
 *  and other copyright owners as documented in the project's IP log.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License&quot￼;
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package org.basinmc.sink.plugin.asm;

import org.basinmc.faucet.plugin.PluginMetadata;
import org.basinmc.faucet.plugin.PluginVersion;
import org.basinmc.faucet.plugin.VersionRange;
import org.basinmc.sink.plugin.SinkPluginLoader;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PluginAnnotationVisitor extends AnnotationVisitor {
    private SinkPluginLoader loader;
    private Consumer<PluginMetadata> callback;
    private PluginMetadata.Builder metadataBuilder = PluginMetadata.builder();

    public PluginAnnotationVisitor(SinkPluginLoader loader, Consumer<PluginMetadata> callback) {
        super(Opcodes.ASM5);
        this.loader = loader;
        this.callback = callback;
    }

    @Override
    public void visit(String name, Object value) {
        switch (name) {
            case "id":
                metadataBuilder.setId((String) value);
                break;
            case "name":
                metadataBuilder.setName((String) value);
                break;
            case "desc":
                metadataBuilder.setDesc((String) value);
                break;
            case "version":
                metadataBuilder.setVersion(PluginVersion.of((String) value));
                break;
            case "dependencies":
                metadataBuilder.setDependencies(processDepends((String[]) value));
                break;
            case "softDependencies":
                metadataBuilder.setSoftDependencies(processDepends((String[]) value));
                break;
            default:
                SinkPluginLoader.logger.error("Somehow this annotation visitor was called on the wrong annotation.");
        }
    }

    @Override
    public void visitEnd() {
        callback.accept(metadataBuilder.build());
        super.visitEnd();
    }

    private Map<String, VersionRange> processDepends(String[] raw) {
        Map<String, VersionRange> depends = new HashMap<>();
        Arrays.stream(raw).forEach(version -> {
            int verDelim = version.indexOf('@');
            int rangeDelim = version.indexOf('-');
            if (rangeDelim != version.length() - 1) {
                VersionRange range = VersionRange.builder()
                        .startInclusive(true)
                        .endInclusive(true)
                        .startBound(PluginVersion.of(version.substring(verDelim + 1, rangeDelim)))
                        .endBound(PluginVersion.of(version.substring(rangeDelim + 1)))
                        .build();
                depends.put(version.substring(0, verDelim), range);
            }

        });
        return depends;
    }
}
