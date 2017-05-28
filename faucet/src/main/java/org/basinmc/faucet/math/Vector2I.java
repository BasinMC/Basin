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
import javax.annotation.Nullable;

/**
 * Two-dimensional integer vector. This should be used for the likes of
 * chunk coordinates.
 */
public final class Vector2I implements Cloneable {

  public static final double EPSILON = 1e-6;

  private final int x;
  private final int y;

  public Vector2I(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public final Vector2I clone() {
    return new Vector2I(x, y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int a = 53;
    a *= (Double.doubleToRawLongBits(this.x) >> 32) ^ Double.doubleToRawLongBits(this.x);
    a *= (Double.doubleToRawLongBits(this.y) >> 32) ^ Double.doubleToRawLongBits(this.y);
    return a;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Vector2I) {
      Vector2I other = (Vector2I) obj;
      return Math.abs((other.x - this.x)) < EPSILON && Math.abs((other.y - this.y)) < EPSILON;
    }
    return false;
  }

  /**
   * Tries to generate a vector from a string. The string should be in the format
   * defined by {@link Vector2I#toString()}
   *
   * @param string The formatted string
   * @return A new vector, or null if the format is incorrect.
   */
  @Nullable
  public static Vector2I fromString(String string) {
    if (!string.startsWith("V2i") || !string.endsWith(";")) {
      return null;
    }
    String[] split = string.replace("V", "").replace(";", "").split(",");
    try {
      int x = Integer.parseInt(split[0]);
      int y = Integer.parseInt(split[1]);
      return new Vector2I(x, y);
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public final String toString() {
    return "V2i" + x + "," + y + ";";
  }

  /**
   * Adds values to this vector
   *
   * @param x The amount to add to the x value
   * @param y The amount to add to the y value
   * @return A new vector
   */
  @Nonnull
  public final Vector2I add(int x, int y) {
    return new Vector2I(this.x + x, this.y + y);
  }

  /**
   * Performs vector addition
   *
   * @param vector The vector to add to this vector
   * @return A new vector
   */
  @Nonnull
  public final Vector2I add(Vector2I vector) {
    return new Vector2I(this.x + vector.x, this.y + vector.y);
  }

  /**
   * Performs scalar multiplication on this vector
   *
   * @param amount The scalar to multiply by
   * @return A new vector
   */
  @Nonnull
  public final Vector2I multiply(int amount) {
    return new Vector2I(x * amount, y * amount);
  }

  // TODO rest of math ops
}
