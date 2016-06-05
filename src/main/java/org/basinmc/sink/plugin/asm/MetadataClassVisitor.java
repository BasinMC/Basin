/*
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
 *
 */

package org.basinmc.sink.plugin.asm;

import org.basinmc.faucet.plugin.Plugin;
import org.basinmc.faucet.plugin.PluginMetadata;
import org.basinmc.sink.plugin.SinkPluginLoader;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.function.Consumer;

public class MetadataClassVisitor extends ClassVisitor {
    private SinkPluginLoader loader;
    private Consumer<PluginMetadata> callback;

    public MetadataClassVisitor(SinkPluginLoader loader, Consumer<PluginMetadata> callback) {
        super(Opcodes.ASM5);
        this.loader = loader;
        this.callback = callback;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals(Type.getDescriptor(Plugin.class))) {
            return new PluginAnnotationVisitor(loader, callback);
        }
        return super.visitAnnotation(desc, visible);
    }
}
