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
package org.basinmc.faucet.plugin;

import java.nio.file.Path;
import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * Manges plugins during their lifecycle.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface PluginManager {

    /**
     * Retrieves a plugin's context based on its group and plugin identifiers.
     *
     * @param pluginId a plugin identifier.
     * @return a plugin context or, if no such plugin could be located, an empty optional.
     */
    @Nonnull
    Optional<PluginContext> getPluginContext(@Nonnull String pluginId);

    /**
     * Retrieves the directory all plugin data is to be stored.
     *
     * @return a path.
     */
    @Nonnull
    Path getStorageDirectory();

    /**
     * Attempts to install a plugin from the specified path.
     *
     * @param pluginPackage a plugin package path.
     * @return a plugin context.
     */
    @Nonnull
    PluginContext install(@Nonnull Path pluginPackage);

    /**
     * Attempts to install a plugin from the specified path using the specified plugin loader.
     *
     * @param pluginPackage a plugin package path.
     * @param loader        a plugin loader.
     * @return a plugin context.
     */
    @Nonnull
    PluginContext install(@Nonnull Path pluginPackage, @Nonnull PluginLoader loader);

    /**
     * Attempts to uninstall a plugin.
     *
     * @param pluginId a plugin identifier.
     */
    void uninstall(@Nonnull String pluginId);
}
