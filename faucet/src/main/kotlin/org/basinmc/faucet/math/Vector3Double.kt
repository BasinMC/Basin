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
data class Vector3Double(override val x: Double = 0.0, override val y: Double = 0.0,
    override val z: Double = 0.0) : Vector3<Double> {

  override val length: Double by lazy {
    sqrt(Math.pow(this.x, 2.0) + Math.pow(this.y, 2.0) + Math.pow(this.z, 2.0))
  }
  override val normalized: Vector3Double by lazy {
    Vector3Double(this.x / this.length, this.y / this.length, this.z / this.length)
  }

  override val int: Vector3<Int>
    get() = Vector3Int(this.x.roundToInt(), this.y.roundToInt(), this.z.roundToInt())
  override val long: Vector3<Long>
    get() = Vector3Long(this.x.roundToLong(), this.y.roundToLong(), this.z.roundToLong())
  override val double: Vector3Double
    get() = this

  override fun plus(addend: Double) = Vector3Double(this.x + addend, this.y + addend,
      this.z + addend)

  override fun plus(addend: Vector2<Double>) = Vector3Double(this.x + addend.x, this.y + addend.y,
      this.z)

  override fun plus(addend: Vector3<Double>) = Vector3Double(this.x + addend.x, this.y + addend.y,
      this.z + addend.z)

  override fun minus(subtrahend: Double) = Vector3Double(this.x - subtrahend, this.y - subtrahend,
      this.z - subtrahend)

  override fun minus(subtrahend: Vector2<Double>) = Vector3Double(this.x - subtrahend.x,
      this.y - subtrahend.y, this.z)

  override fun minus(subtrahend: Vector3<Double>) = Vector3Double(this.x - subtrahend.x,
      this.y - subtrahend.y, this.z - subtrahend.z)

  override fun times(factor: Double) = Vector3Double(this.x * factor, this.y * factor,
      this.z * factor)

  override fun times(factor: Vector2<Double>) = Vector3Double(this.x * factor.x, this.y * factor.y,
      this.z)

  override fun times(factor: Vector3<Double>) = Vector3Double(this.x * factor.x, this.y * factor.y,
      this.z * factor.z)

  override fun div(divisor: Double) = Vector3Double(this.x / divisor, this.y / divisor,
      this.z / divisor)

  override fun div(divisor: Vector2<Double>) = Vector3Double(this.x / divisor.x, this.y / divisor.y,
      this.z)

  override fun div(divisor: Vector3<Double>) = Vector3Double(this.x / divisor.x, this.y / divisor.y,
      this.z / divisor.z)

  override fun rem(divisor: Double) = Vector3Double(this.x % divisor, this.y % divisor,
      this.z % divisor)

  override fun rem(divisor: Vector2<Double>) = Vector3Double(this.x % divisor.x, this.y % divisor.y,
      this.z)

  override fun rem(divisor: Vector3<Double>) = Vector3Double(this.x % divisor.x, this.y % divisor.y,
      this.z % divisor.z)

  companion object : Vector3.Definition<Double> {
    override val zero = Vector3Double()
    override val one = Vector3Double(1.0, 1.0, 1.0)

    override val up = Vector3Double(0.0, 1.0, 0.0)
    override val right = Vector3Double(1.0, 0.0, 0.0)
    override val down = Vector3Double(0.0, -1.0, 0.0)
    override val left = Vector3Double(-1.0, 0.0, 0.0)
    override val forward = Vector3Double(0.0, 0.0, 1.0)
    override val backward = Vector3Double(0.0, 0.0, -1.0)
  }
}
