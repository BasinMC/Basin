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

package org.basinmc.faucet.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

/**
 * Represents a class responsible for loading, transform, and initializing plugins, as well as
 * constructing useful metadata about them. Each plugin will have exactly one PluginLoader, but one
 * PluginLoader can be responsible for any number of plugins;
 */
public interface PluginLoader {
    /**
     * Attempts to queue a plugin (jar) file for loading
     *
     * @param file The jarfile to load
     * @return Whether it was successful
     */
    boolean loadPlugin(File file) throws IOException;

    /**
     * Queues all plugin files in a folder for loading
     *
     * @param folder File representing the folder in which to search
     * @return Whether errors were thrown
     */
    boolean loadPluginsInFolder(Path folder);

    /**
     * Gets the queue of plugin files that have yet to be loaded. Once a plugin is loaded, it will
     * be removed from this queue.
     *
     * @return An ordered list of files
     */
    List<File> getQueuedPluginFiles();

    /**
     * Gets the queue of plugins to be <i>transformed and initialized</i>. The <b>entire</b> loading
     * phase must be complete before this queue begins to be emptied.
     *
     * @return An ordered list of plugin ids.
     */
    List<String> getQueuedPlugins();

    /**
     * Performs bytecode transformation on a loaded (but not initialized) plugin. This is necessary
     * to build the plugin's metadata. TODO: Explain all the transformations
     *
     * @param id The plugin ID to be transformed
     * @return Whether the transformation led to the plugin being sucessfully loaded by the
     * classloader.
     */
    boolean transformPlugin(String id);

    /**
     * Begins the transformation phase, moving through the plugin queue.
     *
     * @return Whether any errors were encountered during transformation.
     */
    boolean transformPlugins();

    /**
     * Builds metadata for a plugin.
     *
     * @param id The plugin for which to build metadata
     * @return The metadata that was built
     */
    PluginMetadata initializePlugin(String id);

    /**
     * Initializes all plugins that have been transformed.
     */
    void initializePlugins();

    /**
     * Get all plugins that have been initialized. This collection should never be removed from,
     * unless a plugin is disabled during runtime.
     *
     * @return A collection of plugin metadatas.
     */
    Collection<PluginMetadata> getLoadedPlugins();
}
