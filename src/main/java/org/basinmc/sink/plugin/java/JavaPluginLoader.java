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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.plugin.PluginContext;
import org.basinmc.faucet.plugin.PluginLoader;
import org.basinmc.faucet.plugin.PluginManager;
import org.basinmc.faucet.plugin.error.PluginLoaderException;
import org.basinmc.sink.plugin.java.ArchiveJavaPluginContext;
import org.basinmc.sink.plugin.java.ExplodedJavaPluginContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.Nonnull;

public class JavaPluginLoader implements PluginLoader {
    public static final Logger logger = LogManager.getFormatterLogger(JavaPluginLoader.class);
    private final PluginManager pluginManager;

    public JavaPluginLoader(@Nonnull PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public PluginContext createContext(@Nonnull Path packagePath) throws PluginLoaderException {
        try {
            if (Files.isDirectory(packagePath)) {
                logger.debug("Loading exploded plugin from %s.", packagePath.toAbsolutePath());
                return new ExplodedJavaPluginContext(packagePath, this.pluginManager.getStorageDirectory());
            } else if (Files.isRegularFile(packagePath)) {
                logger.debug("Loading archive plugin from %s.", packagePath.toAbsolutePath());
                return new ArchiveJavaPluginContext(packagePath, this.pluginManager.getStorageDirectory());
            } else {
                throw new PluginLoaderException("Could not access plugin file/directory: Unknown type");
            }
        } catch (IOException ex) {
            throw new PluginLoaderException("Could not load Java plugin: " + ex.getMessage(), ex);
        }
    }
}
