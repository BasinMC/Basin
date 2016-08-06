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
package org.basinmc.faucet.plugin.loading;

public interface BytecodeAdapter {
    /**
     * Called in attempt to transform the given bytecode <i>before</i> loading. ASM's tree API
     * is definitely the easiest way to modify classes, but note that it is resource intensive.
     * Thus, make sure you check the class name to make sure you need to transform the given class.
     * @param internalName The internal name of the class (using / as the package delimiter)
     * @param superName The name of the given class's superclass
     * @param interfaces An array of internal names for each interface this class implements
     * @param bytecode The bytecode to perform the transformation on
     * @return A transformed version of the provided bytecode
     */
    byte[] adapt(String internalName, String superName, String[] interfaces, byte[] bytecode);
}
