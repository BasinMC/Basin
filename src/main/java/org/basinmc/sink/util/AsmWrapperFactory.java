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
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_8;

/**
 * Okay, so this is a big mess.
 *
 * @param <I> Interface type to make our wrapper implement. Should only have 1 method (and should
 *            probably be a {@link FunctionalInterface}.
 * @param <T> General supertype for property types - interface should have this type as a generic
 *            type parameter
 */
public class AsmWrapperFactory<I, T> {
    private static final Logger logger = LogManager.getLogger(AsmWrapperFactory.class);

    private final Map<Method, Class<? extends I>> methodCache = new HashMap<>();
    private final Class<? extends Annotation> annotation;
    private final Class<I> interfaceType;

    /**
     * Creates a new instance of the wrapper factory
     *
     * @param annotation    The annotation type that methods will be annotated with. Should have a
     *                      single "value" parameter with signature "()Ljava/lang/Class;"
     * @param interfaceType The interface type that the wrapper classes should implement. Should
     *                      conform to the type parameter.
     */
    public AsmWrapperFactory(@Nonnull Class<? extends Annotation> annotation, @Nonnull Class<I> interfaceType) {
        this.annotation = annotation;
        this.interfaceType = interfaceType;
    }

    /**
     * Creates a wrapper class for a method and loads it.
     *
     * @param method The method to create the wrapper for. Must be annotated with the predefined
     *               annotation type.
     * @return The generated class
     */
    @SuppressWarnings("all")
    public Class<? extends I> createWrapper(@Nonnull Method method) {
        if (this.methodCache.containsKey(method)) {
            logger.warn("Attempted to add duplicate wrapper for method " + method.getDeclaringClass().getName() + "#" + method.getName());
            return this.methodCache.get(method);
        }

        if (!method.isAnnotationPresent(this.annotation)) {
            throw new IllegalArgumentException("Attempted to add wrapper for method that is not annotated with @" + this.annotation.getName());
        }
        Annotation a = method.getAnnotation(this.annotation);
        Class<? extends T> propertyType = null;
        try {
            propertyType = (Class<? extends T>) method.getParameterTypes()[0];
        } catch (ClassCastException | IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Illegal method signature " + Type.getMethodDescriptor(method) + " - not compatible with generic arguments.");
        }

        boolean wizardMode = Boolean.getBoolean("org.basinmc.event.sanitycheck") || true;
        ClassWriter cw = new ClassWriter(0);

        MethodVisitor mv;
        String className = method.getDeclaringClass().getSimpleName() + "_" + this.interfaceType.getSimpleName() + this.methodCache.size() + method.getName();
        String classDesc = className.replace(".", "/");
        String targetType = Type.getInternalName(propertyType);
        String callbackType = Type.getInternalName(method.getDeclaringClass());
        String callbackDesc = Type.getDescriptor(method.getDeclaringClass());

        // Copy any annotations found on the base method onto the target class.
        // This is why all annotations that this use should be able to target both.
        AnnotationVisitor av;
        av = cw.visitAnnotation(Type.getDescriptor(annotation), true);
        try {
            for (Method m : annotation.getDeclaredMethods()) {
                Object o = m.invoke(a, null);
                if (o instanceof Enum) {
                    av.visitEnum(m.getName(), Type.getDescriptor(o.getClass()), ((Enum) o).name());
                } else if (o instanceof Class) {
                    av.visit(m.getName(), Type.getDescriptor((Class) o));
                } else if (o instanceof Enum[]) {
                    Enum[] array = (Enum[]) o;
                    AnnotationVisitor arrayVisitor = av.visitArray(m.getName());
                    for (Enum e : array) {
                        String enumDesc = Type.getDescriptor(e.getClass());
                        arrayVisitor.visitEnum(null, enumDesc, e.name());
                    }
                } else if (o instanceof Class[]) {
                    Class[] array = (Class[]) o;
                    AnnotationVisitor arrayVisitor = av.visitArray(m.getName());
                    for (Class clazz : array) {
                        arrayVisitor.visit(null, Type.getDescriptor(clazz));
                    }
                } else {
                    av.visit(m.getName(), o);
                }
            }
        } catch (ReflectiveOperationException e) {
            logger.error("There was an error copying annotations for method " + method.getDeclaringClass().getName() + "." + method.getName());
            logger.error(e);
        } finally {
            av.visitEnd();
        }

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, classDesc, null,
                Type.getInternalName(Object.class), new String[]{Type.getInternalName(this.interfaceType)});

        cw.visitSource("dynamic.java", null);

        // handle field
        cw.visitField(ACC_PUBLIC, "handle", callbackDesc, null, null).visitEnd();

        {
            // constructor
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + callbackDesc + ")V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, classDesc, "handle", callbackDesc);
            mv.visitInsn(RETURN);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }

        {
            // "process" method
            mv = cw.visitMethod(ACC_PUBLIC, "process", Type.getMethodDescriptor(this.interfaceType.getDeclaredMethods()[0]), null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, classDesc, "handle", callbackDesc);
            int i;
            for (i = 0; i < method.getParameterTypes().length; i++) {
                mv.visitVarInsn(ALOAD, i+1);
            }
            mv.visitMethodInsn(INVOKEVIRTUAL, callbackType, method.getName(), Type.getMethodDescriptor(method), false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(i + 1, i + 1);
            mv.visitEnd();
        }

        cw.visitEnd();

        byte[] bytes = cw.toByteArray();
        if (wizardMode) {
            ClassReader cr = new ClassReader(bytes);
            ClassWriter cw1 = new ClassWriter(0);
            cr.accept(cw1, 0);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                stream.write(cw1.toByteArray());
                File f = new File("Test.class");
                f.delete();
                f.createNewFile();
                stream.writeTo(new FileOutputStream(f));
            } catch (IOException e) {
                e.printStackTrace();
            }
            CheckClassAdapter.verify(cr, true, new PrintWriter(System.out));
        }
        Class<? extends I> clazz = new AsmClassLoader(method.getClass().getClassLoader()).define(className, bytes);
        this.methodCache.put(method, clazz);
        return clazz;
    }

    /**
     * Creates a wrapper for each valid method in a class
     *
     * @param holder An instance of the object to register wrappers for.
     */
    public void registerHolder(Object holder) {
        Arrays.stream(holder.getClass().getDeclaredMethods()).filter(method -> method.isAnnotationPresent(this.annotation))
                .filter(method -> Arrays.deepEquals(method.getParameterTypes(), this.interfaceType.getDeclaredMethods()[0].getParameterTypes()))
                .forEach(this::createWrapper);
    }
}
