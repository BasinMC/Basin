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

import com.google.common.collect.ImmutableMap;

import org.basinmc.faucet.plugin.error.PluginException;

import java.nio.file.Path;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * Represents a plugin's context as well as any runtime metadata associated with it.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface PluginContext {

    /**
     * Instructs the context to enter a specific state.
     *
     * @param state a state.
     * @throws IllegalArgumentException when the state is too far away.
     * @throws PluginException          when passing the event to the plugin or initializing the
     *                                  plugin fails.
     */
    void enterState(@Nonnull State state) throws IllegalArgumentException, PluginException;

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
     * Sets the target lifecycle state.
     *
     * @param state a state
     * @throws IllegalArgumentException when a state other than {@link State#LOADED} or {@link
     *                                  State#RUNNING} is passed.
     */
    void setTargetState(@Nonnull State state) throws IllegalArgumentException;

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
        LOADED(0),

        /**
         * Represents a resolved plugin (e.g. all of its dependencies where found and the load order
         * has been designated).
         */
        RESOLVED(1),

        /**
         * Represents a loaded plugin during its first initialization stage.
         */
        PRE_INITIALIZATION(2),

        /**
         * Represents a loaded plugin during its initialization stage.
         */
        INITIALIZATION(3),

        /**
         * Represents a loaded plugin during its post initialization stage.
         */
        POST_INITIALIZATION(4),

        /**
         * Represents an installed and running plugin.
         */
        RUNNING(5),

        /**
         * Represents an installed plugin during its de-initialization stage.
         */
        DE_INITIALIZATION(6);

        private static final Map<Integer, State> stepMap;

        static {
            ImmutableMap.Builder<Integer, State> builder = ImmutableMap.builder();
            {
                for (State state : values()) {
                    builder.put(state.numeric, state);
                }
            }
            stepMap = builder.build();
        }

        private final int numeric;

        State(@Nonnegative int numeric) {
            this.numeric = numeric;
        }

        /**
         * Retrieves the next step to a specific target state.
         *
         * @param target a target state.
         * @return the next step or, if the target is equal to this state, this state.
         */
        @Nonnull
        public State getNextStep(@Nonnull State target) {
            if (this == target) {
                return this;
            }

            int direction = Math.min(1, Math.max(-1, target.numeric - this.numeric));
            return stepMap.get(this.numeric + direction);
        }

        /**
         * Checks whether the passed state is one step or less away from this state.
         *
         * @param state a state.
         * @return true if closest step or equal, false otherwise.
         */
        public boolean isClosestStep(@Nonnull State state) {
            if (this == state) {
                return true;
            }

            return Math.abs(this.numeric - state.numeric) == 1;
        }

        /**
         * Checks whether this state is closer to the target than the supplied state.
         *
         * @param target a target state.
         * @param state  a state to compare against.
         * @return true if closer, false otherwise.
         */
        public boolean isCloserTo(@Nonnull State target, @Nonnull State state) {
            if (this == state) {
                return false;
            }

            if (this == target) {
                return true;
            }

            int i1 = Math.abs((target.numeric - this.numeric));
            int i2 = Math.abs((target.numeric - state.numeric));

            return (i1 < i2);
        }
    }
}
