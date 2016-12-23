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
package org.basinmc.faucet.event;

/**
 * Provides a more specialized event base interface which provides basic logic improvements for
 * events which carry mutable state (mutable in this case refers to the ability of plugins to mutate
 * the state through API methods provided by this event in order to alter subsequent actions).
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface MutableEvent extends Event {

    /**
     * Checks whether the event has been finalized by a previous handler.
     *
     * Finalized events can no longer be altered by subsequent handlers and as such will only be
     * passed to handlers which explicitly express the will to retrieve such finalized event
     * instances.
     *
     * Note: Plugins should only finalize event states when they actually require full authority
     * over an event state while accepting change from another handler is not acceptable to the
     * plugin logic.
     *
     * @return true if finalized, false otherwise.
     */
    boolean isFinalized();

    /**
     * Finalizes the state effectively preventing any further modification.
     *
     * @see #isFinalized() for a more detailed explanation on the topic of finalization.
     */
    void finalizeState();
}
