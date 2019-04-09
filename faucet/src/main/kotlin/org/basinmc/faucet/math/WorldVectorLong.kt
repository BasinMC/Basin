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

import org.basinmc.faucet.world.World
import kotlin.math.roundToLong
import kotlin.math.sqrt

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
data class WorldVectorLong(override val x: Long, override val y: Long, override val z: Long,
    override val world: World) : WorldVector<Long> {

  override val length: Double by lazy {
    sqrt(Math.pow(this.x.toDouble(), 2.0) + Math.pow(this.y.toDouble(), 2.0) + Math.pow(
        this.z.toDouble(), 2.0))
  }
  override val normalized: WorldVector<Long> by lazy {
    WorldVectorLong((this.x / this.length).roundToLong(), (this.y / this.length).roundToLong(),
        (this.z / this.length).roundToLong(), this.world)
  }

  override val long: WorldVectorLong
    get() = this

  override fun plus(addend: Long) = WorldVectorLong(this.x + addend, this.y + addend,
      this.z + addend, this.world)

  override fun plus(addend: Vector2<Long>) = WorldVectorLong(this.x + addend.x, this.y + addend.y,
      this.z, this.world)

  override fun plus(addend: Vector3<Long>) = WorldVectorLong(this.x + addend.x, this.y + addend.y,
      this.z + addend.z, this.world)

  override fun minus(subtrahend: Long) = WorldVectorLong(this.x - subtrahend, this.y - subtrahend,
      this.z - subtrahend, this.world)

  override fun minus(subtrahend: Vector2<Long>) = WorldVectorLong(this.x - subtrahend.x,
      this.y - subtrahend.y, this.z, this.world)

  override fun minus(subtrahend: Vector3<Long>) = WorldVectorLong(this.x - subtrahend.x,
      this.y - subtrahend.y, this.z - subtrahend.z, this.world)

  override fun times(factor: Long) = WorldVectorLong(this.x * factor, this.y * factor,
      this.z * factor, this.world)

  override fun times(factor: Vector2<Long>) = WorldVectorLong(this.x * factor.x, this.y * factor.y,
      this.z, this.world)

  override fun times(factor: Vector3<Long>) = WorldVectorLong(this.x * factor.x, this.y * factor.y,
      this.z * factor.z, this.world)

  override fun div(divisor: Long) = WorldVectorLong(this.x / divisor, this.y / divisor,
      this.z / divisor, this.world)

  override fun div(divisor: Vector2<Long>) = WorldVectorLong(this.x / divisor.x, this.y / divisor.y,
      this.z, this.world)

  override fun div(divisor: Vector3<Long>) = WorldVectorLong(this.x / divisor.x, this.y / divisor.y,
      this.z / divisor.z, this.world)

  override fun rem(divisor: Long) = WorldVectorLong(this.x % divisor, this.y % divisor,
      this.z % divisor, this.world)

  override fun rem(divisor: Vector2<Long>) = WorldVectorLong(this.x % divisor.x, this.y % divisor.y,
      this.z, this.world)

  override fun rem(divisor: Vector3<Long>) = WorldVectorLong(this.x % divisor.x, this.y % divisor.y,
      this.z % divisor.z, this.world)
}
