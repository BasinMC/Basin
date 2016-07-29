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

import org.basinmc.faucet.Basin;
import org.basinmc.faucet.plugin.VersionOnly;
import org.basinmc.faucet.plugin.loading.BytecodeAdapter;
import org.basinmc.sink.Launcher;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class VersionRestrictingClassAdapter implements BytecodeAdapter {
    @Override
    public byte[] adapt(byte[] bytecode) { // TODO Handle method adapting
        ClassReader cr = new ClassReader(bytecode);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        for (AnnotationNode an : ((List<AnnotationNode>) cn.visibleAnnotations)) {
            if (!an.desc.equals(Type.getDescriptor(VersionOnly.class))) continue;
                    Map<String, Object> values = annotationValues(an);
                    String version = (String) values.get("version");
                    if (!isInRange(version, Basin.MINECRAFT_VERSION)) {
                        List<AnnotationNode> altNodes = (List<AnnotationNode>) values.get("alt");
                        for (AnnotationNode alt : altNodes) {
                            Map<String, String> altMapping = new HashMap<>();
                            annotationValues(alt).forEach((p1, p2) -> altMapping.put(p1, (String) p2));
                            for (String requestedVersion : altMapping.keySet()) {
                                if (isInRange(requestedVersion, Basin.MINECRAFT_VERSION)) {
                                    String classInternalName = altMapping.get(requestedVersion).replace(".", "/");
                                    try {
                                        ClassReader repIn = new ClassReader(Launcher.class.getResourceAsStream("/" + classInternalName));
                                        ClassWriter out = new ClassWriter(0);
                                        repIn.accept(out, 0);
                                        return out.toByteArray();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
        }
        return bytecode;
    }

    private static Map<String, Object> annotationValues(AnnotationNode visitor) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < visitor.values.size(); i+=2) {
            map.put((String) visitor.values.get(i), visitor.values.get(i+1));
        }
        return map;
    }

    private static boolean isInRange(String rangeString, String version) {
        if (!rangeString.contains("-")) {
            return rangeString.equalsIgnoreCase(version);
        } else {
            String[] split = rangeString.split("-");
            if (split.length != 2) {
                throw new IllegalArgumentException(rangeString + " is not a valid version range!");
            }
            String lower = split[0].split("\\.").length != 3 ? split[0].concat(".0") : split[0];
            String upper = split[1].split("\\.").length != 3 ? split[1].concat(".0") : split[1];

            String compare;
            if (version.split("\\.").length != 3) {
                compare = version.concat(".0");
            } else {
                compare = version;
            }

            if(upper.equals(compare) || lower.equals(compare)) {
                return true;
            }

            String[] lowerParts = lower.split("\\.");
            String[] upperParts = upper.split("\\.");
            String[] compareParts = compare.split("\\.");

            if (Integer.parseInt(lowerParts[0]) < Integer.parseInt(compareParts[0])
                    && Integer.parseInt(upperParts[0]) > Integer.parseInt(compare)) {
                return true;
            }
        }
        return false;
    }
}
