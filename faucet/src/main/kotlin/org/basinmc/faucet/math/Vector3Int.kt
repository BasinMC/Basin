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
import kotlin.math.sqrt

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
data class Vector3Int(override val x: Int = 0, override val y: Int = 0, override val z: Int = 0) :
    Vector3<Int> {

  override val length: Double by lazy {
    sqrt(Math.pow(this.x.toDouble(), 2.0) + Math.pow(this.y.toDouble(), 2.0) + Math.pow(
        this.z.toDouble(), 2.0))
  }
  override val normalized: Vector3Int by lazy {
    Vector3Int((this.x / this.length).roundToInt(), (this.y / this.length).roundToInt(),
        (this.z / this.length).roundToInt())
  }

  override val int: Vector3Int
    get() = this

  override fun plus(addend: Int) = Vector3Int(this.x + addend, this.y + addend, this.z + addend)
  override fun plus(addend: Vector2<Int>) = Vector3Int(this.x + addend.x, this.y + addend.y, this.z)
  override fun plus(addend: Vector3<Int>) = Vector3Int(this.x + addend.x, this.y + addend.y,
      this.z + addend.z)

  override fun minus(subtrahend: Int) = Vector3Int(this.x - subtrahend, this.y - subtrahend,
      this.z - subtrahend)

  override fun minus(subtrahend: Vector2<Int>) = Vector3Int(this.x - subtrahend.x,
      this.y - subtrahend.y, this.z)

  override fun minus(subtrahend: Vector3<Int>) = Vector3Int(this.x - subtrahend.x,
      this.y - subtrahend.y, this.z - subtrahend.z)

  override fun times(factor: Int) = Vector3Int(this.x * factor, this.y * factor, this.z * factor)
  override fun times(factor: Vector2<Int>) = Vector3Int(this.x * factor.x, this.y * factor.y,
      this.z)

  override fun times(factor: Vector3<Int>) = Vector3Int(this.x * factor.x, this.y * factor.y,
      this.z * factor.z)

  override fun div(divisor: Int) = Vector3Int(this.x / divisor, this.y / divisor, this.z / divisor)
  override fun div(divisor: Vector2<Int>) = Vector3Int(this.x / divisor.x, this.y / divisor.y,
      this.z)

  override fun div(divisor: Vector3<Int>) = Vector3Int(this.x / divisor.x, this.y / divisor.y,
      this.z / divisor.z)

  override fun rem(divisor: Int) = Vector3Int(this.x % divisor, this.y % divisor, this.z % divisor)
  override fun rem(divisor: Vector2<Int>) = Vector3Int(this.x % divisor.x, this.y % divisor.y,
      this.z)

  override fun rem(divisor: Vector3<Int>) = Vector3Int(this.x % divisor.x, this.y % divisor.y,
      this.z % divisor.z)

  companion object : Vector3.Definition<Int> {
    override val zero = Vector3Int()
    override val one = Vector3Int(1, 1, 1)

    override val up = Vector3Int(0, 1, 0)
    override val right = Vector3Int(1, 0, 0)
    override val down = Vector3Int(0, -1, 0)
    override val left = Vector3Int(-1, 0, 0)
    override val forward = Vector3Int(0, 0, 1)
    override val backward = Vector3Int(0, 0, -1)
  }
}
