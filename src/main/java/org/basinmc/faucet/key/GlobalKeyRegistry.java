/*
 * Copyright 2017 Hex <hex@hex.lc>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.basinmc.faucet.key;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an application-wide registration of {@link Key} namespaces.
 */
public interface GlobalKeyRegistry {
    /**
     * Attempts to create a registry with the given namespace.
     *
     * @param namespace the namespace to create
     * @return a new registry
     * @throws IllegalArgumentException if the given namespace is already in use
     */
    @Nonnull
    KeyRegistry create(String namespace) throws IllegalArgumentException;

    /**
     * Safely create a registry for the calling bundle, using its name as a namespace.
     *
     * @return a new registry
     */
    @Nonnull
    KeyRegistry create();

    /**
     * Attempts to look up a key in a given namespace.
     *
     * @param namespace the namespace to search
     * @param name the key name
     * @return a key, or null if none is present
     */
    @Nullable
    Key lookup(String namespace, String name);
}
