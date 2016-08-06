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

import org.basinmc.faucet.lang.Localize;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;
import java.util.Map;

public class LocalizationClassAdapter extends SinkClassAdapter {
    private static final String PREFIX = Character.toString((char) 0x1A); // SUB
    private static final String ESCAPE = Character.toString((char) 0x10); // DLE

    @Override
    public boolean isAdaptable(String internalName, String superName, String[] interfaces, byte[] bytecode) {
        return true; // We don't know the annotations on it.
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClassNode adaptClass(ClassNode node) {
        for (FieldNode fn : (List<FieldNode>) node.fields) {
            if (!fn.desc.equals(Type.getDescriptor(String.class))) continue;
            for (AnnotationNode an : (List<AnnotationNode>) fn.visibleAnnotations) {
                if (an.desc.equals(Type.getDescriptor(Localize.class))) {
                    Map<String, Object> values = annotationValues(an);
                    StringBuilder sb = new StringBuilder();
                    sb.append(PREFIX);
                    if ("$PROVIDED$".equals(values.get("value"))) {
                        sb.append(((String)fn.value).replace((char) 0x1A, (char) 0x10)
                            .replace(ESCAPE, ESCAPE + ESCAPE));
                    } else {
                        sb.append(((String)values.get("value")).replace((char) 0x1A, (char) 0x10)
                                .replace(ESCAPE, ESCAPE + ESCAPE));
                    }
                    // TODO rest of annotation parameters
                    fn.value = sb.toString();
                }
            }
        }
        return node;
    }
}
