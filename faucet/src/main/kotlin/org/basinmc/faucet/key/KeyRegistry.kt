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
package org.basinmc.faucet.key

/**
 * Holds registrations for keys. One instance is created for each namespace.
 */
interface KeyRegistry {

  /**
   * Get the namespace for which this registry holds keys.
   */
  val namespace: String

  /**
   * Get a set of keys present in this registry
   */
  val keys: Set<Key>

  /**
   * Attempt to create a key.
   *
   * @param name the name which the key will have
   * @return a new [Key] object
   * @throws IllegalStateException if a key with the given namespace and name is already
   * registered.
   */
  @Throws(IllegalStateException::class)
  fun create(name: String): Key

  /**
   * Attempt to retrieve an already-created key.
   *
   * @param name the name to search for
   * @return an existing key, or null if none has been registered
   */
  operator fun get(name: String): Key?

  /**
   * Attempts to retrieve an already-created key, or creates one if none exists.
   *
   * @param name the name of the key
   * @return an existing key, or a new one if it didn't previously exist
   */
  fun getOrCreate(name: String): Key

}
