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

import javax.annotation.Nonnull;

/**
 * Represents a plugin's context as well as any runtime metadata associated with it.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface PluginContext {

    /**
     * Retrieves the plugin's metadata.
     *
     * @return the metadata.
     */
    @Nonnull
    PluginMetadata getMetadata();

    /**
     * Retrieves a path to the source plugin file or a directory of resources which make up the
     * plugin source.
     *
     * @return a source path.
     */
    @Nonnull
    Path getSource();

    /**
     * Retrieves the state the plugin is currently in.
     *
     * @return a plugin state.
     */
    @Nonnull
    State getState();

    /**
     * Retrieves the path to the directory which is supposed to host all permanently stored data for
     * this plugin.
     *
     * @return a directory path.
     */
    @Nonnull
    Path getStorageDirectory();

    /**
     * Retrieves the state a plugin is supposed to ultimately reach.
     *
     * This method is guaranteed to return either {@link State#RUNNING} or {@link State#LOADED}
     * where {@link State#RUNNING} indicates a plugin that is supposed to be loaded and {@link
     * State#LOADED} indicates a plugin that is supposed to be unloaded.
     *
     * @return a target plugin state.
     */
    @Nonnull
    State getTargetState();

    /**
     * Provides a list of valid plugin states.
     *
     * Plugins generally traverse phases in order as follows: Loaded, Resolved, Pre Initialization,
     * Initialization, Post Initialization, Running, De-Initialization
     */
    enum State {

        /**
         * Represents a loaded plugin which has been verified and constructed by the system.
         */
        LOADED,

        /**
         * Represents a resolved plugin (e.g. all of its dependencies where found and the load order
         * has been designated).
         */
        RESOLVED,

        /**
         * Represents a loaded plugin during its first initialization stage.
         */
        PRE_INITIALIZATION,

        /**
         * Represents a loaded plugin during its initialization stage.
         */
        INITIALIZATION,

        /**
         * Represents a loaded plugin during its post initialization stage.
         */
        POST_INITIALIZATION,

        /**
         * Represents an installed and running plugin.
         */
        RUNNING,

        /**
         * Represents an installed plugin during its de-initialization stage.
         */
        DE_INITIALIZATION
    }
}
