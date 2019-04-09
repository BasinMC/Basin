/*
 * Copyright 2016 Hex <hex@hex.lc>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License&quot￼;
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
 *
 */
package org.basinmc.faucet.capability

/**
 * Super-interface for anything stored in a [Capabilities] object.
 */
interface Capability {

  /**
   * Checks if this capability is registered in the server capability registry.
   */
  val isRegistered: Boolean

  /**
   * Get the types this capability uses as parameters
   *
   * @return an ordered array, or an empty array if no parameters are present
   */
  val parameterTypes: Array<Class<*>>

  /**
   * Represents a parameter type for a specific [Capability]
   *
   * @param <T> The type of data it holds
   */
  interface Parameter<T> {

    /**
     * Set the specified value to a nullable value.
     *
     * @param value the value to set to
     */
    fun set(value: T?)

    /**
     * Retrieve the parameter's value
     */
    fun get(): T?
  }
}
