/*
 * Copyright 2019 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.faucet.util

import java.util.*

/**
 * Represents an arbitrary bit mask which expresses the state of one or more predefined flags.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
abstract class BitMask<M : BitMask<M>> protected constructor(private val mask: Int) : Iterable<M> {

  abstract val definition: Definition<M>

  /**
   * Retrieves the total amount of set flags within this mask.
   *
   * @return an amount of flags.
   */
  val size = Integer.bitCount(this.mask)

  /**
   * Evaluates whether this mask contains the indicated flag.
   *
   * @param state an arbitrary flag.
   * @return true if set, false otherwise.
   */
  fun has(state: M) = if (this.javaClass != state.javaClass) { // TODO: Does this seem sensible?
    false
  } else {
    this.mask and (state as BitMask<*>).mask == (state as BitMask<*>).mask
  }

  /**
   * Sets one or more flags within this mask.
   *
   * @param state an arbitrary flag.
   */
  fun set(state: M): M {
    if (this.javaClass != state.javaClass) {
      throw IllegalArgumentException(
          "Expected state flag to be of type " + this.javaClass.name + " but got " + state.javaClass.name)
    }

    return this.definition.newInstance(this.mask xor (state as BitMask<*>).mask)
  }

  /**
   * Un-Sets one or more flags within this mask.
   *
   * @param state an arbitrary flag.
   */
  fun unset(state: M): M {
    if (this.javaClass != state.javaClass) {
      throw IllegalArgumentException(
          "Expected state flag to be of type " + this.javaClass.name + " but got " + state.javaClass.name)
    }

    return this.definition.newInstance(this.mask and (state as BitMask<*>).mask.inv())
  }

  /**
   * {@inheritDoc}
   */
  override fun iterator(): Iterator<M> {
    return this.definition.values
        .filter(this::has)
        .iterator()
  }

  interface Definition<M : BitMask<M>> {

    /**
     * Constructs a mutated bitmask instance for this particular type.
     *
     * @param mask an arbitrary bitmask.
     * @return a mutated instance.
     */
    fun newInstance(mask: Int): M

    /**
     * Retrieves a list of all permitted values within this mask type.
     *
     * @return a list of permitted values.
     */
    val values: Collection<M>
  }


  /**
   * {@inheritDoc}
   */
  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other !is BitMask<*>) {
      return false
    }
    val bitMask = other as BitMask<*>?
    return this.mask == bitMask!!.mask
  }

  /**
   * {@inheritDoc}
   */
  override fun hashCode(): Int {
    return Objects.hash(this.mask)
  }
}
