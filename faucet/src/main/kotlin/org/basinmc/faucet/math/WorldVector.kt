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

/**
 * Represents a position or direction in 3D world space.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
interface WorldVector<N : Number> : Vector3<N> {

  val world: World

  override val normalized: WorldVector<N>

  override val int: WorldVector<Int>
    get() = WorldVectorInt(this.x.toInt(), this.y.toInt(), this.z.toInt(), this.world)
  override val long: WorldVector<Long>
    get() = WorldVectorLong(this.x.toLong(), this.y.toLong(), this.z.toLong(), this.world)
  override val float: WorldVector<Float>
    get() = WorldVectorFloat(this.x.toFloat(), this.y.toFloat(), this.z.toFloat(), this.world)
  override val double: WorldVector<Double>
    get() = WorldVectorDouble(this.x.toDouble(), this.y.toDouble(), this.z.toDouble(), this.world)

  override fun plus(addend: N): WorldVector<N>
  override fun plus(addend: Vector2<N>): WorldVector<N>
  override fun plus(addend: Vector3<N>): WorldVector<N>

  override fun minus(subtrahend: N): WorldVector<N>
  override fun minus(subtrahend: Vector2<N>): WorldVector<N>
  override fun minus(subtrahend: Vector3<N>): WorldVector<N>

  override fun times(factor: N): WorldVector<N>
  override fun times(factor: Vector2<N>): WorldVector<N>
  override fun times(factor: Vector3<N>): WorldVector<N>

  override fun div(divisor: N): WorldVector<N>
  override fun div(divisor: Vector2<N>): WorldVector<N>
  override fun div(divisor: Vector3<N>): WorldVector<N>

  override fun rem(divisor: N): WorldVector<N>
  override fun rem(divisor: Vector2<N>): WorldVector<N>
  override fun rem(divisor: Vector3<N>): WorldVector<N>

  operator fun component4(): World = this.world
}
