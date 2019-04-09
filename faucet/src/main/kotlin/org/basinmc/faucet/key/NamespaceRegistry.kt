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
 * Represents an application-wide registration of [Key] namespaces.
 */
interface NamespaceRegistry {

  /**
   * Attempts to create a registry with the given namespace.
   *
   * @param namespace the namespace to create
   * @return a new registry
   * @throws IllegalArgumentException if the given namespace is already in use
   */
  @Throws(IllegalArgumentException::class)
  fun create(namespace: String): KeyRegistry

  /**
   * Safely create a registry for the calling bundle, using its name as a namespace.
   *
   * @return a new registry
   */
  // TODO: Extension specific identification or smart package based id for our purposes?
  @Deprecated("Requires discussion")
  fun create(): KeyRegistry

  /**
   * Retrieves the key registry for a given namespace.
   */
  operator fun get(namespace: String): KeyRegistry?

  /**
   * Attempts to look up a key in a given namespace.
   *
   * @param namespace the namespace to search
   * @param name the key name
   * @return a key, or null if none is present
   */
  operator fun get(namespace: String, name: String): Key?
}
