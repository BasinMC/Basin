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
package org.basinmc.faucet.event;

import org.basinmc.faucet.event.handler.EventHandler;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Provides an event management system which will automatically post events to all registered
 * services of type {@link EventHandler}.
 */
@ThreadSafe
public interface EventBus {

    /**
     * Posts an event to the event bus. Each handler configured to
     * accept a supertype or equivalent type of event will receive
     * the event.
     * @param event The even to post
     * @param <T> The type of event
     */
    <T extends Event> void post(@Nonnull T event);
}
