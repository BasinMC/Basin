/*
 * Copyright 2016 Hex <hex@hex.lc>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License&quot￼;
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
 *
 */
package org.basinmc.faucet.math

/**
 * Relative directions.
 */
enum class Direction(

    /**
     * Get the transformation vector for moving one place in this direction. Remember
     * that you can perform scalar multiplication on this vector to signify movement of a
     * value different than 1 block.
     *
     * @return a 3-dimensional transformation vector
     */
    val vector: Vector3Int) {

  /**
   * Down, towards negative-y
   */
  DOWN(Vector3Int.down),

  /**
   * Up, towards positive-y
   */
  UP(Vector3Int.up),

  /**
   * North, towards negative-z
   */
  NORTH(Vector3Int.backward),

  /**
   * South, towards positive-z
   */
  SOUTH(Vector3Int.forward),

  /**
   * West, towards negative-x
   */
  WEST(Vector3Int.left),

  /**
   * East, towards positive-x
   */
  EAST(Vector3Int.right)
}
