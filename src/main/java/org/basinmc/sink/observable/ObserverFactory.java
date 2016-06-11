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
package org.basinmc.sink.observable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.observable.ObservableProperty;
import org.basinmc.faucet.observable.Observe;
import org.basinmc.faucet.observable.Observer;
import org.basinmc.sink.util.AsmClassLoader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import static org.objectweb.asm.Opcodes.*;

// TODO Investigate if we can abstract this out a bit for event logic - it'd work almost exactly the same (this was "inspired" by forge event code)
public class ObserverFactory {
    private static Map<Method, Class<?>> methodCache = new HashMap<>();
    private static Logger logger = LogManager.getLogger("Sink Observer Factory");

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends Observer<? extends T>> createObserver(@Nonnull Method method) {
        if (methodCache.containsKey(method)) {
            logger.warn("Attempted to add duplicate observer for method " + method.getDeclaringClass().getName() + "#" + method.getName());
            return (Class<? extends Observer<? extends T>>) methodCache.get(method);
        }

        if (!method.isAnnotationPresent(Observe.class)) {
            throw new IllegalArgumentException("Attempted to add observer for method that is not annotated with @Observe.");
        }
        Observe annotation = method.getAnnotation(Observe.class);
        Class<? extends ObservableProperty> propertyType = annotation.value();

        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;
        String className = method.getDeclaringClass().getName() + "_" + "Observer" + methodCache.size() + method.getName();
        String classDesc = className.replace(".", "/");
        String targetType = Type.getInternalName(propertyType);
        String callbackType = Type.getInternalName(method.getDeclaringClass());
        String callbackDesc = Type.getDescriptor(method.getDeclaringClass());

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER | ACC_SYNTHETIC, classDesc,
                "Ljava/lang/Object;Lorg/basinmc/faucet/observable/Observer<"+ Type.getDescriptor(propertyType) +">;",
                Type.getInternalName(Object.class), new String[]{Type.getInternalName(Observer.class)});

        cw.visitSource("dynamic.java", null);

        // handle field
        cw.visitField(ACC_PUBLIC, "handle", callbackDesc, null, null).visitEnd();

        // constructor
        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + callbackDesc + ")V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, classDesc, "handle", callbackDesc);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();

        // "change" method
        mv = cw.visitMethod(ACC_PUBLIC, "change", Type.getMethodDescriptor(Observer.class.getDeclaredMethods()[0]), null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETFIELD, classDesc, "handle", callbackDesc);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, callbackType, method.getName(), Type.getMethodDescriptor(method), false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(4, 4);
        mv.visitEnd();

        cw.visitEnd();
        Class<? extends Observer<? extends T>> clazz = new AsmClassLoader(method.getClass().getClassLoader()).define(className, cw.toByteArray());
        methodCache.put(method, clazz);
        return clazz;
    }

    public static void registerObservers(Object holder) {
        Arrays.stream(holder.getClass().getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Observe.class))
                .filter(method -> Arrays.deepEquals(method.getParameterTypes(), new Class<?>[]{ObservableProperty.class, Object.class, Object.class}))
                .forEach(ObserverFactory::createObserver);
    }
}
