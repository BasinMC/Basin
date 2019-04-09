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
data class Vector3Float(override val x: Float = 0f, override val y: Float = 0f,
    override val z: Float = 0f) : Vector3<Float> {

  override val length: Double by lazy {
    sqrt(Math.pow(this.x.toDouble(), 2.0) + Math.pow(this.y.toDouble(), 2.0) + Math.pow(
        this.z.toDouble(), 2.0))
  }
  override val normalized: Vector3Float by lazy {
    Vector3Float((this.x / this.length).toFloat(), (this.y / this.length).toFloat(),
        (this.z / this.length).toFloat())
  }

  override val int: Vector3<Int>
    get() = Vector3Int(this.x.roundToInt(), this.y.roundToInt(), this.z.roundToInt())
  override val long: Vector3<Long>
    get() = Vector3Long(this.x.roundToLong(), this.y.roundToLong(), this.z.roundToLong())
  override val float: Vector3Float
    get() = this

  override fun plus(addend: Float) = Vector3Float(this.x + addend, this.y + addend, this.z + addend)
  override fun plus(addend: Vector2<Float>) = Vector3Float(this.x + addend.x, this.y + addend.y,
      this.z)

  override fun plus(addend: Vector3<Float>) = Vector3Float(this.x + addend.x, this.y + addend.y,
      this.z + addend.z)

  override fun minus(subtrahend: Float) = Vector3Float(this.x - subtrahend, this.y - subtrahend,
      this.z - subtrahend)

  override fun minus(subtrahend: Vector2<Float>) = Vector3Float(this.x - subtrahend.x,
      this.y - subtrahend.y, this.z)

  override fun minus(subtrahend: Vector3<Float>) = Vector3Float(this.x - subtrahend.x,
      this.y - subtrahend.y, this.z - subtrahend.z)

  override fun times(factor: Float) = Vector3Float(this.x * factor, this.y * factor,
      this.z * factor)

  override fun times(factor: Vector2<Float>) = Vector3Float(this.x * factor.x, this.y * factor.y,
      this.z)

  override fun times(factor: Vector3<Float>) = Vector3Float(this.x * factor.x, this.y * factor.y,
      this.z * factor.z)

  override fun div(divisor: Float) = Vector3Float(this.x / divisor, this.y / divisor,
      this.z / divisor)

  override fun div(divisor: Vector2<Float>) = Vector3Float(this.x / divisor.x, this.y / divisor.y,
      this.z)

  override fun div(divisor: Vector3<Float>) = Vector3Float(this.x / divisor.x, this.y / divisor.y,
      this.z / divisor.z)

  override fun rem(divisor: Float) = Vector3Float(this.x % divisor, this.y % divisor,
      this.z % divisor)

  override fun rem(divisor: Vector2<Float>) = Vector3Float(this.x % divisor.x, this.y % divisor.y,
      this.z)

  override fun rem(divisor: Vector3<Float>) = Vector3Float(this.x % divisor.x, this.y % divisor.y,
      this.z % divisor.z)

  companion object : Vector3.Definition<Float> {
    override val zero = Vector3Float()
    override val one = Vector3Float(1f, 1f, 1f)

    override val up = Vector3Float(0f, 1f, 0f)
    override val right = Vector3Float(1f, 0f, 0f)
    override val down = Vector3Float(0f, -1f, 0f)
    override val left = Vector3Float(-1f, 0f, 0f)
    override val forward = Vector3Float(0f, 0f, 1f)
    override val backward = Vector3Float(0f, 0f, -1f)
  }
}
