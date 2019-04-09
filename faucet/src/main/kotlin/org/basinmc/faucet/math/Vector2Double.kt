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

import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
data class Vector2Double(override val x: Double = 0.0, override val y: Double = 0.0) :
    Vector2<Double> {

  override val length: Double by lazy {
    sqrt(Math.pow(this.x, 2.0) + Math.pow(this.y, 2.0))
  }
  override val normalized: Vector2Double by lazy {
    Vector2Double(this.x / this.length, this.y / this.length)
  }

  override val int: Vector2Int
    get() = Vector2Int(this.x.roundToInt(), this.y.roundToInt())
  override val long: Vector2Long
    get() = Vector2Long(this.x.roundToLong(), this.y.roundToLong())
  override val double: Vector2<Double>
    get() = this

  override fun plus(addend: Double) = Vector2Double(this.x + addend, this.y + addend)
  override fun plus(addend: Vector2<Double>) = Vector2Double(this.x + addend.x, this.y + addend.y)

  override fun minus(subtrahend: Double) = Vector2Double(this.x - subtrahend, this.y - subtrahend)
  override fun minus(subtrahend: Vector2<Double>) = Vector2Double(this.x - subtrahend.x,
      this.y - subtrahend.y)

  override fun times(factor: Double) = Vector2Double(this.x * factor, this.y * factor)
  override fun times(factor: Vector2<Double>) = Vector2Double(this.x * factor.x, this.y * factor.y)

  override fun div(divisor: Double) = Vector2Double(this.x / divisor, this.y / divisor)
  override fun div(divisor: Vector2<Double>) = Vector2Double(this.x / divisor.x, this.y / divisor.y)

  override fun rem(divisor: Double) = Vector2Double(this.x % divisor, this.y % divisor)
  override fun rem(divisor: Vector2<Double>) = Vector2Double(this.x % divisor.x, this.y % divisor.y)

  companion object : Vector2.Definition<Double> {
    override val zero = Vector2Double()
    override val one = Vector2Double(1.0, 1.0)

    override val up = Vector2Double(0.0, 1.0)
    override val right = Vector2Double(1.0, 0.0)
    override val down = Vector2Double(0.0, -1.0)
    override val left = Vector2Double(-1.0, 0.0)
  }
}
