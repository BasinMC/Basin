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
package org.basinmc.sink.transformers;

import org.basinmc.faucet.plugin.loading.BytecodeAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SinkClassAdapter implements BytecodeAdapter {
    private List<SinkClassAdapter> adapters = new ArrayList<>(); // We want to be able to determine order here.

    public SinkClassAdapter() {
        adapters.add(new VersionRestrictingClassAdapter());
        adapters.add(new LocalizationClassAdapter());
    }

    @Override
    public byte[] adapt(String internalName, String superName, String[] interfaces, byte[] bytecode) {
        List<SinkClassAdapter> validAdapters = adapters.stream().filter(adapter -> adapter.isAdaptable(internalName, superName, interfaces, bytecode))
                .collect(Collectors.toList());
        if (!validAdapters.isEmpty()) {
            ClassReader cr = new ClassReader(bytecode);
            ClassNode cn = new ClassNode();
            cr.accept(cn, 0);
            for (SinkClassAdapter adapter : validAdapters) {
                cn = adapter.adaptClass(cn);
            }
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cn.accept(cw);
            return cw.toByteArray();
        }
        return bytecode;
    }

    public abstract boolean isAdaptable(String internalName, String superName, String[] interfaces, byte[] bytecode);
    public abstract ClassNode adaptClass(ClassNode node);

    protected void register(SinkClassAdapter adapter) {
        adapters.add(adapter);
    }

    static Map<String, Object> annotationValues(AnnotationNode visitor) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < visitor.values.size(); i+=2) {
            map.put((String) visitor.values.get(i), visitor.values.get(i+1));
        }
        return map;
    }
}
