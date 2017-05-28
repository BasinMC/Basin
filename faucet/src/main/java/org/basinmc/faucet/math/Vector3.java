/*
 *  Copyright 2016 __0x277F <0x277F@gmail.com>
 *  and other copyright owners as documented in the project's IP log.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License&quot￼;
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.basinmc.faucet.math;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an immutable vector in three dimensions. This class is not
 * final by design.
 */
public class Vector3 implements Cloneable {

  public static final double EPSILON = 1e-6;

  private final double x;
  private final double y;
  private final double z;

  public Vector3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public final Vector3 clone() {
    // We don't need to call super.clone() because the method is final.
    return new Vector3(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int hashCode() {
    // This uses a similar algorithm to Bukkit's because I can't be arsed to make my own.
    int a = 53;
    a *= (Double.doubleToRawLongBits(this.x) >> 32) ^ Double.doubleToRawLongBits(this.x);
    a *= (Double.doubleToRawLongBits(this.y) >> 32) ^ Double.doubleToRawLongBits(this.y);
    a *= (Double.doubleToRawLongBits(this.z) >> 32) ^ Double.doubleToRawLongBits(this.z);
    return a;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public final String toString() {
    return "V" + x + "," + y + "," + z + ";";
  }

  /**
   * Tries to generate a string from a vector. The string should be in the format
   * defined by {@link Vector3#toString()}
   *
   * @param string The formatted string
   * @return A new vector, or null if the format is incorrect.
   */
  @Nullable
  public static Vector3 fromString(String string) {
    if (!string.startsWith("V") || !string.endsWith(";")) {
      return null;
    }
    String[] split = string.replace("V", "").replace(";", "").split(",");
    try {
      double x = Double.parseDouble(split[0]);
      double y = Double.parseDouble(split[1]);
      double z = Double.parseDouble(split[2]);
      return new Vector3(x, y, z);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public final double getX() {
    return x;
  }

  public final double getY() {
    return y;
  }

  public final double getZ() {
    return z;
  }

  /**
   * Adds values to this vector
   *
   * @param x The amount to add to the x value
   * @param y The amount to add to the y value
   * @param z The amount to add to the z value
   * @return A new vector
   */
  @Nonnull
  public final Vector3 add(double x, double y, double z) {
    return new Vector3(this.x + x, this.y + y, this.z + z);
  }

  /**
   * Performs vector addition
   *
   * @param vector The vector to add to this vector
   * @return A new vector
   */
  @Nonnull
  public final Vector3 add(Vector3 vector) {
    return new Vector3(this.x + vector.x, this.y + vector.y, this.z + vector.z);
  }

  /**
   * Performs scalar multiplication on this vector
   *
   * @param amount The scalar to multiply by
   * @return A new vector
   */
  @Nonnull
  public final Vector3 multiply(double amount) {
    return new Vector3(x * amount, y * amount, z * amount);
  }

  /**
   * Computes the cross product of two vectors
   *
   * @param other The other vector for which to compute the cross product
   * @return A new vector representing the cross product
   */
  @Nonnull
  public final Vector3 crossProduct(Vector3 other) {
    return new Vector3(
        (this.y * other.z) - (this.z * other.y),
        (this.z * other.x) - (this.x * other.z),
        (this.x * other.y) - (this.y * other.x)
    );
  }

  /**
   * Computes the dot product of two vectors
   *
   * @param other The other vector for which to compute the dot product
   * @return The dot product
   */
  public final double dotProduct(Vector3 other) {
    return (this.x * other.x) + (this.y * other.y) + (this.z * other.z);
  }

  /**
   * Computes the midpoint between two vectors
   *
   * @param other The other vector for which to compute the midpoint
   * @return A new vector representing the midpoint
   */
  @Nonnull
  public final Vector3 midpoint(Vector3 other) {
    return new Vector3(
        (this.x + other.x) / 2,
        (this.y + other.y) / 2,
        (this.z + other.z) / 2
    );
  }

  /**
   * Gets the magnitude of this vector
   *
   * @return A double representing the magnitude (length)
   */
  public final double length() {
    return Math.sqrt((x * x) + (y * y) + (z * z));
  }

  /**
   * Gets the square of the magnitude of this vector. If at all possible,
   * use this instead of {@link Vector3#length()} as this is significantly
   * faster.
   *
   * @return A double representing the square of the magnitude
   */
  public final double lengthSquared() {
    return (x * x) + (y * y) + (z * z);
  }

  /**
   * Determines whether this vector is aligned to a block.
   *
   * @return True if it is.
   */
  public boolean isBlockAligned() {
    return this instanceof BlockAlignedVector3 ||
        (this.x == Math.floor(this.x) && this.y == Math.floor(this.y) && this.z == Math
            .floor(this.z)
            && !Double.isInfinite(this.x) && !Double.isInfinite(this.y) && !Double
            .isInfinite(this.z));
  }

  /**
   * Creates a 2-dimensional vector from this vector by flooring all values then flattening
   * each.
   *
   * @return a new 2-dimensional integer vector
   */
  @Nonnull
  public Vector2I flatten() {
    return new Vector2I((int) x, (int) z);
  }
}
