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

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Holds registrations for keys. One instance is created for each namespace.
 */
public interface KeyRegistry {

  /**
   * Get the namespace for which this registry holds keys.
   */
  @Nonnull
  String getNamespace();

  /**
   * Attempt to create a key.
   *
   * @param name the name which the key will have
   * @return a new {@link Key} object
   * @throws IllegalStateException if a key with the given namespace and name is already
   * registered.
   */
  @Nonnull
  Key create(@Nonnull String name) throws IllegalStateException;

  /**
   * Attempt to retrieve an already-created key.
   *
   * @param name the name to search for
   * @return an existing key, or null if none has been registered
   */
  @Nullable
  Key get(@Nonnull String name);

  /**
   * Attempts to retrieve an already-created key, or creates one if none exists.
   *
   * @param name the name of the key
   * @return an existing key, or a new one if it didn't previously exist
   */
  @Nonnull
  Key getOrCreate(@Nonnull String name);

  /**
   * Get a set of keys present in this registry
   */
  @Nonnull
  Set<Key> getKeys();

}
