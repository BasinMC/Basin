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

import org.basinmc.faucet.plugin.ClassLoaderPluginContext;
import org.basinmc.faucet.plugin.Plugin;
import org.basinmc.faucet.plugin.PluginContext;
import org.basinmc.faucet.plugin.PluginMetadata;
import org.basinmc.faucet.plugin.PluginVersion;
import org.basinmc.faucet.plugin.VersionRange;
import org.basinmc.faucet.plugin.error.PluginException;
import org.basinmc.faucet.plugin.loading.BytecodeAdapter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class AbstractJavaPluginContext implements ClassLoaderPluginContext {
    private final Path source;
    private final Map<String, PluginContext> wiredPluginDependencies = new HashMap<>();
    private State state = State.LOADED;
    private State targetState = State.RUNNING;
    private Object instance;
    private MethodHandle instanceFactory;

    public AbstractJavaPluginContext(@Nonnull Path source) throws IOException {
        this.source = source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterState(@Nonnull State state) throws IllegalArgumentException, PluginException {
        if ((this.state != State.DE_INITIALIZATION || state != State.LOADED) && state.isClosestStep(this.state)) {
            throw new IllegalArgumentException("Cannot switch from " + this.state + " to " + state + ": Too far away");
        }

        switch (state) {
            case RESOLVED:
                if (instanceFactory == null) {
                    // FIXME: Add support for constructor level injection?
                    try {
                        Class<?> mainClass = this.getMainClass();
                        this.instanceFactory = MethodHandles.publicLookup().findConstructor(mainClass, MethodType.methodType(void.class));
                    } catch (IllegalAccessException | NoSuchMethodException ex) {
                        throw new PluginException("Cannot construct main plugin class: " + ex.getMessage(), ex);
                    }
                }

                try {
                    this.instance = this.instanceFactory.invoke();
                } catch (Throwable ex) {
                    throw new PluginException("Could not call plugin factory: " + ex.getMessage(), ex);
                }
                break;
            case PRE_INITIALIZATION:
            case INITIALIZATION:
            case POST_INITIALIZATION:
            case DE_INITIALIZATION:
                // FIXME: This needs the event system
                throw new UnsupportedOperationException("Not implemented");
        }
    }

    /**
     * Retrieves the current plugin instance.
     *
     * @return an instance.
     */
    @Nullable
    public Object getInstance() {
        return this.instance;
    }

    /**
     * Retrieves the main plugin class to initialize when the appropriate state is reached.
     *
     * @return a plugin type.
     *
     * @throws PluginException when loading the class fails.
     */
    @Nonnull
    protected abstract Class<?> getMainClass() throws PluginException;

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Optional<PluginContext> getDependencyContext(@Nonnull String pluginId) {
        return Optional.ofNullable(this.wiredPluginDependencies.get(pluginId));
    }

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
    public State getTargetState() {
        return this.targetState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTargetState(@Nonnull State state) throws IllegalArgumentException {
        if (state != State.LOADED && state != State.RUNNING) {
            throw new IllegalArgumentException("Invalid target state: " + state);
        }

        this.targetState = state;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Set<PluginContext> getWiredDependencies() {
        return Collections.unmodifiableSet(new HashSet<>(this.wiredPluginDependencies.values()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void wire(@Nonnull PluginContext context) {
        this.wiredPluginDependencies.put(context.getMetadata().getId(), context);
    }

    /**
     * Provides a class visitor capable of locating a plugin main class as well as processing its
     * metadata.
     */
    protected static class LocatorClassVisitor extends ClassVisitor {
        private String className = null;
        private Set<String> transformers = new HashSet<>();
        private PluginAnnotationVisitor annotationVisitor = null;

        public LocatorClassVisitor() {
            super(Opcodes.ASM5);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (Type.getDescriptor(Plugin.class).equals(desc)) {
                return this.annotationVisitor = new PluginAnnotationVisitor();
            }

            return super.visitAnnotation(desc, visible);
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
         * Retrieves a set of all classes implemented {@link BytecodeAdapter}
         *
         * @return A set.
         */
        public Set<String> getTransformers() {
            return transformers;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            if (Arrays.asList(interfaces).contains(Type.getInternalName(BytecodeAdapter.class))) {
                transformers.add(name);
            }
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
