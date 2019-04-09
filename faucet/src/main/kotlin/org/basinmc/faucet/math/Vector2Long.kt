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
package org.basinmc.faucet.math

import kotlin.math.roundToLong
import kotlin.math.sqrt

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
data class Vector2Long(override val x: Long = 0, override val y: Long = 0) : Vector2<Long> {

  override val length: Double by lazy {
    sqrt(Math.pow(this.x.toDouble(), 2.0) + Math.pow(this.y.toDouble(), 2.0))
  }
  override val normalized: Vector2Long by lazy {
    Vector2Long((this.x / this.length).roundToLong(), (this.y / this.length).roundToLong())
  }

  override val long: Vector2<Long>
    get() = this

  override fun plus(addend: Long) = Vector2Long(this.x + addend, this.y + addend)
  override fun plus(addend: Vector2<Long>) = Vector2Long(this.x + addend.x, this.y + addend.y)

  override fun minus(subtrahend: Long) = Vector2Long(this.x - subtrahend, this.y - subtrahend)
  override fun minus(subtrahend: Vector2<Long>) = Vector2Long(this.x - subtrahend.x,
      this.y - subtrahend.y)

  override fun times(factor: Long) = Vector2Long(this.x * factor, this.y * factor)
  override fun times(factor: Vector2<Long>) = Vector2Long(this.x * factor.x, this.y * factor.y)

  override fun div(divisor: Long) = Vector2Long(this.x / divisor, this.y / divisor)
  override fun div(divisor: Vector2<Long>) = Vector2Long(this.x / divisor.x, this.y / divisor.y)

  override fun rem(divisor: Long) = Vector2Long(this.x % divisor, this.y % divisor)
  override fun rem(divisor: Vector2<Long>) = Vector2Long(this.x % divisor.x, this.y % divisor.y)

  companion object : Vector2.Definition<Long> {
    override val zero = Vector2Long()
    override val one = Vector2Long(1, 1)

    override val up = Vector2Long(0, 1)
    override val right = Vector2Long(1, 0)
    override val down = Vector2Long(0, -1)
    override val left = Vector2Long(-1, 0)
  }
}
