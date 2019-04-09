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
data class Vector2Float(override val x: Float = 0f, override val y: Float = 0f) : Vector2<Float> {

  override val length: Double by lazy {
    sqrt(Math.pow(this.x.toDouble(), 2.0) + Math.pow(this.y.toDouble(), 2.0))
  }
  override val normalized: Vector2Float by lazy {
    Vector2Float((this.x / this.length).toFloat(), (this.y / this.length).toFloat())
  }

  override val int: Vector2Int
    get() = Vector2Int(this.x.roundToInt(), this.x.roundToInt())
  override val long: Vector2Long
    get() = Vector2Long(this.x.roundToLong(), this.y.roundToLong())
  override val float: Vector2<Float>
    get() = this

  override fun plus(addend: Float) = Vector2Float(this.x + addend, this.y + addend)
  override fun plus(addend: Vector2<Float>) = Vector2Float(this.x + addend.x, this.y + addend.y)

  override fun minus(subtrahend: Float) = Vector2Float(this.x - subtrahend, this.y - subtrahend)
  override fun minus(subtrahend: Vector2<Float>) = Vector2Float(this.x - subtrahend.x,
      this.y - subtrahend.y)

  override fun times(factor: Float) = Vector2Float(this.x * factor, this.y * factor)
  override fun times(factor: Vector2<Float>) = Vector2Float(this.x * factor.x, this.y * factor.y)

  override fun div(divisor: Float) = Vector2Float(this.x / divisor, this.y / divisor)
  override fun div(divisor: Vector2<Float>) = Vector2Float(this.x / divisor.x, this.y / divisor.y)

  override fun rem(divisor: Float) = Vector2Float(this.x % divisor, this.y % divisor)
  override fun rem(divisor: Vector2<Float>) = Vector2Float(this.x % divisor.x, this.y % divisor.y)

  companion object : Vector2.Definition<Float> {
    override val zero = Vector2Float()
    override val one = Vector2Float(1f, 1f)

    override val up = Vector2Float(0f, 1f)
    override val right = Vector2Float(1f, 0f)
    override val down = Vector2Float(0f, -1f)
    override val left = Vector2Float(-1f, 0f)
  }
}
