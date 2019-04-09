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
 * Represents a position or direction within 2D space.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
interface Vector2<N : Number> {

  val x: N
  val y: N

  val length: Double
  val normalized: Vector2<N>

  val int: Vector2<Int>
    get() = Vector2Int(this.x.toInt(), this.y.toInt())
  val long: Vector2<Long>
    get() = Vector2Long(this.x.toLong(), this.y.toLong())
  val float: Vector2<Float>
    get() = Vector2Float(this.x.toFloat(), this.y.toFloat())
  val double: Vector2<Double>
    get() = Vector2Double(this.x.toDouble(), this.y.toDouble())

  operator fun plus(addend: N): Vector2<N>
  operator fun plus(addend: Vector2<N>): Vector2<N>

  operator fun minus(subtrahend: N): Vector2<N>
  operator fun minus(subtrahend: Vector2<N>): Vector2<N>

  operator fun times(factor: N): Vector2<N>
  operator fun times(factor: Vector2<N>): Vector2<N>

  operator fun div(divisor: N): Vector2<N>
  operator fun div(divisor: Vector2<N>): Vector2<N>

  operator fun rem(divisor: N): Vector2<N>
  operator fun rem(divisor: Vector2<N>): Vector2<N>

  operator fun component1() = this.x
  operator fun component2() = this.x

  interface Definition<N : Number> {
    val zero: Vector2<N>
    val one: Vector2<N>

    val up: Vector2<N>
    val right: Vector2<N>
    val down: Vector2<N>
    val left: Vector2<N>
  }
}
