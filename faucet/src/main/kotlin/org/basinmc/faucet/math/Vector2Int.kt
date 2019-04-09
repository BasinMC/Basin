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

import java.lang.Math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
data class Vector2Int(override val x: Int = 0, override val y: Int = 0) : Vector2<Int> {

  override val length: Double by lazy {
    sqrt(pow(this.x.toDouble(), 2.0) + pow(this.y.toDouble(), 2.0))
  }
  override val normalized: Vector2Int by lazy {
    Vector2Int((this.x / this.length).roundToInt(), (this.y / this.length).roundToInt())
  }

  override val int: Vector2Int
    get() = this

  override fun plus(addend: Int) = Vector2Int(this.x + addend, this.y + addend)
  override fun plus(addend: Vector2<Int>) = Vector2Int(this.x + addend.x, this.y + addend.y)

  override fun minus(subtrahend: Int) = Vector2Int(this.x - subtrahend, this.y - subtrahend)
  override fun minus(subtrahend: Vector2<Int>) = Vector2Int(this.x - subtrahend.x,
      this.y - subtrahend.y)

  override fun times(factor: Int) = Vector2Int(this.x * factor, this.y * factor)
  override fun times(factor: Vector2<Int>) = Vector2Int(this.x * factor.x, this.y * factor.y)

  override fun div(divisor: Int) = Vector2Int(this.x / divisor, this.y / divisor)
  override fun div(divisor: Vector2<Int>) = Vector2Int(this.x / divisor.x, this.y / divisor.y)

  override fun rem(divisor: Int) = Vector2Int(this.x % divisor, this.y % divisor)
  override fun rem(divisor: Vector2<Int>) = Vector2Int(this.x % divisor.x, this.y % divisor.y)

  companion object : Vector2.Definition<Int> {
    override val zero = Vector2Int()
    override val one = Vector2Int(1, 1)

    override val up = Vector2Int(0, 1)
    override val right = Vector2Int(1, 0)
    override val down = Vector2Int(0, -1)
    override val left = Vector2Int(-1, 0)
  }
}
