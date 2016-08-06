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
package org.basinmc.sink.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class TransformationDataVisitor extends ClassVisitor {
    Data data;

    public TransformationDataVisitor() {
        super(Opcodes.ASM5);
    }

    static class Data {
        final String internalName;
        final String[] interfaces;
        final String superName;

        Data(String internalName, String[] interfaces, String superName) {
            this.internalName = internalName;
            this.interfaces = interfaces;
            this.superName = superName;
        }
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.data = new Data(name, interfaces, superName);
    }
}
