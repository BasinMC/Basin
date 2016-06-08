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
package org.basinmc.sink.plugin;

import org.basinmc.faucet.plugin.PluginContext;
import org.basinmc.faucet.plugin.PluginLoader;
import org.basinmc.faucet.plugin.PluginManager;
import org.basinmc.faucet.plugin.error.PluginException;
import org.basinmc.faucet.plugin.error.PluginLoaderException;
import org.basinmc.sink.SinkServer;
import org.basinmc.sink.plugin.java.JavaPluginLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class SinkPluginManager implements PluginManager {
    private final SinkServer server;
    private final PluginLoader defaultPluginLoader = new JavaPluginLoader(this); // TODO: Allow plugins to replace this?
    private final Map<String, PluginContext> pluginContextMap = new HashMap<>();

    public SinkPluginManager(@Nonnull SinkServer server) {
        this.server = server;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Optional<PluginContext> getPluginContext(@Nonnull String pluginId) {
        return Optional.ofNullable(this.pluginContextMap.get(pluginId));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Path getStorageDirectory() {
        // TODO: Generally it might be smart to keep the data out of the user's way and store our configuration files in a dedicated directory
        return this.server.getBaseDirectory().resolve("data");
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public PluginContext install(@Nonnull Path pluginPackage) throws PluginLoaderException {
        return this.install(pluginPackage, this.defaultPluginLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public PluginContext install(@Nonnull Path pluginPackage, @Nonnull PluginLoader loader) throws PluginLoaderException {
        PluginContext ctx = loader.createContext(pluginPackage);
        this.pluginContextMap.put(ctx.getMetadata().getId(), ctx);

        // TODO: Iterate through all stages of plugin initialization
        // TODO: Move above's code into a private method in order to call it from the directory version of this method

        return ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Set<PluginContext> installDirectory(@Nonnull Path pluginDirectory) throws PluginLoaderException {
        try {
            final Set<PluginContext> pluginContexts = new HashSet<>();

            Files.walk(pluginDirectory).forEach((p) -> {
                try {
                    pluginContexts.add(this.install(p));
                } catch (PluginLoaderException ex) {
                    throw new RuntimeException(ex);
                }
            });

            return pluginContexts;
        } catch (IOException ex) {
            throw new PluginLoaderException("Could not access plugin directory \"" + pluginDirectory.toAbsolutePath().toString() + "\": " + ex.getMessage());
        } catch (RuntimeException ex) {
            Throwable cause = ex.getCause();

            if (cause instanceof PluginLoaderException) {
                throw (PluginLoaderException) ex.getCause();
            }

            throw ex;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Set<PluginContext> installDirectory(@Nonnull Path pluginDirectory, @Nonnull Predicate<PluginException> exceptionPredicate) throws PluginLoaderException {
        try {
            final Set<PluginContext> pluginContexts = new HashSet<>();
            Iterator<Path> it = Files.walk(pluginDirectory).iterator();

            while (it.hasNext()) {
                Path p = it.next();

                try {
                    pluginContexts.add(this.install(p));
                } catch (PluginLoaderException ex) {
                    if (!exceptionPredicate.test(ex)) {
                        return pluginContexts;
                    }
                }
            }

            return pluginContexts;
        } catch (IOException ex) {
            throw new PluginLoaderException("Could not access plugin directory \"" + pluginDirectory.toAbsolutePath().toString() + "\": " + ex.getMessage());
        } catch (RuntimeException ex) {
            Throwable cause = ex.getCause();

            if (cause instanceof PluginLoaderException) {
                throw (PluginLoaderException) ex.getCause();
            }

            throw ex;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall(@Nonnull String pluginId) {
        // TODO: Call framework event
        // TODO: Iterate through all relevant plugin phases

        this.pluginContextMap.remove(pluginId);
    }
}
