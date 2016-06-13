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

import org.basinmc.faucet.plugin.ClassLoaderPluginContext;

import java.net.URL;
import java.net.URLClassLoader;

import javax.annotation.Nonnull;

/**
 * Provides a simple class loader implementation which is capable of loading classes from within
 * the plugin itself as well as any of its wired dependencies.
 */
class PluginClassLoader extends URLClassLoader {
    private AbstractJavaPluginContext context;

    public PluginClassLoader(AbstractJavaPluginContext context, @Nonnull URL url) {
        super(new URL[]{url});
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> loadClass(@Nonnull String name) throws ClassNotFoundException {
        try {
            return super.loadClass(name);
        } catch (ClassNotFoundException ex) {
            // @formatter:off
            return context.getWiredDependencies().stream()
                    .filter((d) -> d instanceof ClassLoaderPluginContext)
                    .map((d) -> {
                        ClassLoaderPluginContext ctx = (ClassLoaderPluginContext) d;

                        try {
                            return ctx.getClassLoader().loadClass(name);
                        } catch (ClassNotFoundException e) {
                            return null;
                        }
                    })
                    .filter((d) -> d != null)
                    .findAny()
                    .orElseThrow(() -> new ClassNotFoundException(name));
            // @formatter:on
        }
    }

    /**
     * A simple delegating classloader to allow for runtime rewriting of classes.
     */
    class AsmClassLoader extends ClassLoader {
        public AsmClassLoader(ClassLoader parent) {
            super(parent);
        }

        public Class<?> define(String name, byte[] bytecode) {
            return defineClass(name, bytecode, 0, bytecode.length);
        }
    }
}
