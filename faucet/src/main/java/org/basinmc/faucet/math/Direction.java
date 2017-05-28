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
package org.basinmc.faucet.math;

import javax.annotation.Nonnull;

/**
 * Relative directions.
 */
public enum Direction {
  /**
   * Down, towards negative-y
   */
  DOWN(0, -1, 0),

  /**
   * Up, towards positive-y
   */
  UP(0, 1, 0),

  /**
   * North, towards negative-z
   */
  NORTH(0, 0, -1),

  /**
   * South, towards positive-z
   */
  SOUTH(0, 0, 1),

  /**
   * West, towards negative-x
   */
  WEST(-1, 0, 0),

  /**
   * East, towards positive-x
   */
  EAST(1, 0, 0);

  private final Vector3 vector;

  Direction(Vector3 vector) {
    this.vector = vector;
  }

  Direction(double x, double y, double z) {
    this(new Vector3(x, y, z));
  }

  /**
   * Get the transformation vector for moving one place in this direction. Remember
   * that you can perform scalar multiplication on this vector to signify movement of a
   * value different than 1 block.
   *
   * @return a 3-dimensional transformation vector
   */
  @Nonnull
  Vector3 vector() {
    return vector;
  }
}
