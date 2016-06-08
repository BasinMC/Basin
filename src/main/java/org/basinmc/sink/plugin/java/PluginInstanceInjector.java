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

package org.basinmc.sink.plugin.java;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Tables;

import org.basinmc.faucet.Server;
import org.basinmc.faucet.plugin.Instance;
import org.basinmc.faucet.plugin.PluginContext;
import org.basinmc.faucet.plugin.PluginInstance;
import org.basinmc.sink.SinkServer;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginInstanceInjector {
    // TODO This is piss-ugly, but I can't think of a better way to do it.
    private Map<Class<?>, Object> injections = new HashMap<>();
    private HashBasedTable<String, Boolean, Object> injectionTable = HashBasedTable.create();
    private byte[] bytes;

    public PluginInstanceInjector(SinkServer server, byte[] bytes) {
        this.bytes = bytes;
        this.injections.put(Server.class, server);
    }

    public void transform() {
        ClassReader cr = new ClassReader(bytes);
        InstanceInjectorVisitor iiv = new InstanceInjectorVisitor();
        cr.accept(iiv, 0);
        ClassWriter cw = new ClassWriter(0);
    }

    class InstanceInjectorVisitor extends ClassVisitor {
        private String desc;

        public InstanceInjectorVisitor() {
            super(Opcodes.ASM5);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            List<String> i = Arrays.asList(interfaces);
            i.add(Type.getDescriptor(PluginInstance.class));
            this.desc = signature;
            super.visit(version, access, name, signature, superName, (String[]) i.toArray());
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            return new InstanceFieldVisitor(desc, (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC);
        }

        class InstanceFieldVisitor extends FieldVisitor {
            private boolean isStatic;
            private String desc;

            public InstanceFieldVisitor(String desc, boolean isStatic) {
                super(Opcodes.ASM5);
                this.isStatic = isStatic;
                this.desc = desc;
            }

            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if (desc.equals(Type.getDescriptor(Instance.class))) {
                    return new AnnotationVisitor(Opcodes.ASM5) {
                        @Override
                        public void visit(String name, Object value) {
                            if (name.equals("value")) {
                                if (value instanceof Class) {
                                    Object o = injections.get(value);
                                    if (o != null) {
                                        injectionTable.put(InstanceFieldVisitor.this.desc, isStatic, o);
                                    } else {
                                        if (Type.getDescriptor((Class) value).equals(InstanceInjectorVisitor.this.desc)) {
                                            injectionTable.put(InstanceFieldVisitor.this.desc, isStatic, InstanceInjectorVisitor.this.desc);
                                        }
                                    }
                                }
                            }
                        }
                    };
                } else {
                    return super.visitAnnotation(desc, visible);
                }
            }
        }
    }

    public <T> T getInjection(Class<? extends T> clazz) {
        return (T) injections.get(clazz);
    }
}
