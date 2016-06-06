/*
 * Copyright 2016 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.basinmc.sink.plugin.java;

import com.google.common.collect.ImmutableMap;

import org.basinmc.faucet.plugin.Plugin;
import org.basinmc.faucet.plugin.PluginContext;
import org.basinmc.faucet.plugin.PluginMetadata;
import org.basinmc.faucet.plugin.PluginVersion;
import org.basinmc.faucet.plugin.VersionRange;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import jdk.internal.org.objectweb.asm.Type;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class AbstractJavaPluginContext implements PluginContext {
    private final Path source;
    private final Path storageDirectory;
    private State state = State.LOADED;
    private State targetState = State.RUNNING;

    public AbstractJavaPluginContext(@Nonnull Path source, @Nonnull Path storageDirectory) throws IOException {
        this.source = source;
        this.storageDirectory = storageDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public abstract PluginMetadata getMetadata();

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Path getSource() {
        return this.source;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public State getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Path getStorageDirectory() {
        return this.storageDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public State getTargetState() {
        return this.targetState;
    }

    /**
     * Provides a class visitor capable of locating a plugin main class as well as processing its
     * metadata.
     */
    protected static class LocatorClassVisitor extends ClassVisitor {
        private String className = null;
        private PluginAnnotationVisitor annotationVisitor = null;

        public LocatorClassVisitor() {
            super(Opcodes.ASM5);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
            if (Type.getDescriptor(Plugin.class).equals(desc)) {
                return this.annotationVisitor = new PluginAnnotationVisitor();
            }

            return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
        }

        /**
         * Retrieves the main plugin class name or, if no plugin class was located (yet), an empty
         * optional.
         *
         * @return a plugin class name.
         */
        @Nonnull
        public Optional<String> getClassName() {
            return Optional.ofNullable(this.className);
        }

        /**
         * Retrieves the transformed plugin metadata.
         *
         * @return a plugin metadata representation.
         */
        @Nonnull
        public Optional<PluginMetadata> getMetadata() {
            return Optional.ofNullable(this.annotationVisitor).map((v) -> v.metadata);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.className = name.replace('/', '.');
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void visitEnd() {
            if (this.annotationVisitor == null || this.annotationVisitor.metadata == null) {
                this.className = null;
                this.annotationVisitor = null;
            }
        }

        /**
         * Visits all plugin annotations in the main class.
         */
        private static class PluginAnnotationVisitor extends AnnotationVisitor {
            private final PluginMetadata.Builder builder = new PluginMetadata.Builder();
            private PluginMetadata metadata = null;

            public PluginAnnotationVisitor() {
                super(Opcodes.ASM5);
            }

            /**
             * Processes an array of encoded dependencies.
             *
             * @param dependencies a dependency array.
             * @return a map of dependencies and their respective version ranges.
             */
            @Nonnull
            private Map<String, VersionRange> processDependencies(@Nonnull String[] dependencies) {
                ImmutableMap.Builder<String, VersionRange> mapBuilder = ImmutableMap.builder();
                {
                    for (String dependency : dependencies) {
                        int versionIndex = dependency.indexOf('@');

                        if (versionIndex == -1) {
                            // TODO: Custom Exceptions
                            throw new IllegalArgumentException("Invalid dependency description: " + dependency);
                        }

                        mapBuilder.put(dependency.substring(0, versionIndex - 1), PluginVersion.range(dependency.substring(versionIndex + 1)));
                    }
                }
                return mapBuilder.build();
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void visit(@Nonnull String name, @Nonnull Object value) {
                switch (name) {
                    case "id":
                        this.builder.setId((String) value);
                        break;
                    case "name":
                        this.builder.setName((String) value);
                        break;
                    case "desc":
                        this.builder.setDesc((String) value);
                        break;
                    case "version":
                        this.builder.setVersion(PluginVersion.of((String) value));
                        break;
                    case "dependencies":
                        this.builder.setDependencies(this.processDependencies((String[]) value));
                        break;
                    case "softDependencies":
                        this.builder.setSoftDependencies(this.processDependencies((String[]) value));
                        break;
                }
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void visitEnd() {
                super.visitEnd();

                this.metadata = this.builder.build();
            }
        }
    }
}
