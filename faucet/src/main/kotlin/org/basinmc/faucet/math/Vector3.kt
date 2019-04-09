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

/**
 * Represents a position or direction within 3D space.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
interface Vector3<N : Number> : Vector2<N> {

  val z: N

  override val normalized: Vector3<N>

  override val int: Vector3<Int>
    get() = Vector3Int(this.x.toInt(), this.y.toInt(), this.z.toInt())
  override val long: Vector3<Long>
    get() = Vector3Long(this.x.toLong(), this.y.toLong(), this.z.toLong())
  override val float: Vector3<Float>
    get() = Vector3Float(this.x.toFloat(), this.y.toFloat(), this.z.toFloat())
  override val double: Vector3<Double>
    get() = Vector3Double(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())

  override fun plus(addend: N): Vector3<N>
  override fun plus(addend: Vector2<N>): Vector3<N>
  fun plus(addend: Vector3<N>): Vector3<N>

  override fun minus(subtrahend: N): Vector3<N>
  override fun minus(subtrahend: Vector2<N>): Vector3<N>
  fun minus(subtrahend: Vector3<N>): Vector3<N>

  override fun times(factor: N): Vector3<N>
  override fun times(factor: Vector2<N>): Vector3<N>
  fun times(factor: Vector3<N>): Vector3<N>

  override fun div(divisor: N): Vector3<N>
  override fun div(divisor: Vector2<N>): Vector3<N>
  fun div(divisor: Vector3<N>): Vector3<N>

  override fun rem(divisor: N): Vector3<N>
  override fun rem(divisor: Vector2<N>): Vector3<N>
  fun rem(divisor: Vector3<N>): Vector3<N>

  operator fun component3(): N = this.z

  interface Definition<N : Number> : Vector2.Definition<N> {
    override val zero: Vector3<N>
    override val one: Vector3<N>

    override val up: Vector3<N>
    override val right: Vector3<N>
    override val down: Vector3<N>
    override val left: Vector3<N>
    val forward: Vector3<N>
    val backward: Vector3<N>
  }
}
