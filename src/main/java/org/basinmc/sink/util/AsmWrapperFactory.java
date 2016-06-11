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
package org.basinmc.sink.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import static org.objectweb.asm.Opcodes.*;

/**
 * Okay, so this is a big mess.
 * @param <I> Interface type to make our wrapper implement. Should only have 1 method (and should probably be a {@link FunctionalInterface}.
 * @param <T> General supertype for property types - interface should have this type as a generic type parameter
 */
public class AsmWrapperFactory<I, T> {
    private Map<Method, Class<? extends I>> methodCache = new HashMap<>();
    private static Logger logger = LogManager.getLogger("Sink Wrapper Factory");
    private Class<? extends Annotation> annotation;
    private Class<I> interfaceType;

    /**
     * Creates a new instance of the wrapper factory
     * @param annotation The annotation type that methods will be annotated with. Should have a single "value" parameter with signature "()Ljava/lang/Class;"
     * @param interfaceType The interface type that the wrapper classes should implement. Should conform to the type parameter.
     */
    public AsmWrapperFactory(@Nonnull Class<? extends Annotation> annotation, @Nonnull Class<I> interfaceType) {
        try {
            annotation.getDeclaredMethod("value", Class.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Annotation type " + annotation.getName() + " must have a \"value\" method with the signature ()Ljava/lang/class;");
        }
        this.annotation = annotation;
        this.interfaceType = interfaceType;
    }

    /**
     * Creates a wrapper class for a method and loads it.
     * @param method The method to create the wrapper for. Must be annotated with the predefined annotation type.
     * @return The generated class
     */
    @SuppressWarnings("all")
    public Class<? extends I> createWrapper(@Nonnull Method method) {
        if (methodCache.containsKey(method)) {
            logger.warn("Attempted to add duplicate wrapper for method " + method.getDeclaringClass().getName() + "#" + method.getName());
            return methodCache.get(method);
        }

        if (!method.isAnnotationPresent(annotation)) {
            throw new IllegalArgumentException("Attempted to add wrapper for method that is not annotated with @" + annotation.getName());
        }
        Annotation a = method.getAnnotation(annotation);
        Class<? extends T> propertyType = null;
        try {
            propertyType = (Class<? extends T>) annotation.getClass().getDeclaredMethod("value", Class.class).invoke(a);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // This will /never/ happen. Ever. If it happens I'll eat my socks.
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Illegal annotation type " + a.getClass().getName() + " - not compatible with generic arguments.");
        }

        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;
        String className = method.getDeclaringClass().getName() + "_" + interfaceType.getName() + methodCache.size() + method.getName();
        String classDesc = className.replace(".", "/");
        String targetType = Type.getInternalName(propertyType);
        String callbackType = Type.getInternalName(method.getDeclaringClass());
        String callbackDesc = Type.getDescriptor(method.getDeclaringClass());

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER | ACC_SYNTHETIC, classDesc,
                "Ljava/lang/Object;L" + Type.getInternalName(interfaceType) + "<"+ Type.getDescriptor(propertyType) +">;",
                Type.getInternalName(Object.class), new String[]{Type.getInternalName(interfaceType)});

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
        mv = cw.visitMethod(ACC_PUBLIC, "process", Type.getMethodDescriptor(interfaceType.getDeclaredMethods()[0]), null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETFIELD, classDesc, "handle", callbackDesc);
        int i;
        for (i = 0; i < method.getParameterTypes().length; i++) {
            mv.visitVarInsn(ALOAD, i);
        }
        mv.visitMethodInsn(INVOKEVIRTUAL, callbackType, method.getName(), Type.getMethodDescriptor(method), false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(i+1, i+1);
        mv.visitEnd();

        cw.visitEnd();
        Class<? extends I> clazz = new AsmClassLoader(method.getClass().getClassLoader()).define(className, cw.toByteArray());
        methodCache.put(method, clazz);
        return clazz;
    }

    /**
     * Creates a wrapper for each valid method in a class
     * @param holder An instance of the object to register wrappers for.
     */
    public void registerHolder(Object holder) {
        Arrays.stream(holder.getClass().getDeclaredMethods()).filter(method -> method.isAnnotationPresent(annotation))
                .filter(method -> Arrays.deepEquals(method.getParameterTypes(), interfaceType.getDeclaredMethods()[0].getParameterTypes()))
                .forEach(this::createWrapper);
    }
}
