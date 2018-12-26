/*
 * Copyright 2018 Johannes Donath  <johannesd@torchmind.com>
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
package org.basinmc.faucet.util;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Objects;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public class VersionRange {

  private final Version start;
  private final boolean startInclusive;
  private final Version end;
  private final boolean endInclusive;

  public VersionRange(@NonNull String range) {
    var elements = range.split(",");
    if (elements.length > 2) {
      throw new IllegalArgumentException("Malformed version range: " + range);
    }

    var firstElement = elements[0];
    var lastElement = elements[elements.length - 1];

    var prefix = firstElement.charAt(0);
    var suffix = lastElement.charAt(lastElement.length() - 1);

    if (prefix == '[' || prefix == '(') {
      firstElement = firstElement.substring(1);
    }
    if (suffix == ']' || suffix == ')') {
      lastElement = lastElement.substring(0, lastElement.length() - 1);
    }

    if (elements.length == 2) {
      if (prefix != '[' && prefix != '(') {
        throw new IllegalArgumentException(
            "Illegal range parameters: Expected either ( or [ prefix");
      }
      if (suffix != ']' && suffix != ')') {
        throw new IllegalArgumentException(
            "Illegal range parameters: Expected either ) or ] suffix");
      }

      this.startInclusive = prefix == '[';
      this.endInclusive = suffix == ']';
    } else {
      this.startInclusive = prefix != '(';
      this.endInclusive = suffix != ')';
    }

    try {
      this.start = new Version(firstElement);
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("Illegal start of range: " + firstElement, ex);
    }

    if (elements.length == 1) {
      this.end = this.start;
    } else {
      try {
        this.end = new Version(lastElement);
      } catch (IllegalArgumentException ex) {
        throw new IllegalArgumentException("Illegal end of range: " + lastElement, ex);
      }
    }
  }

  /**
   * Evaluates whether a given version is part of this version range.
   *
   * @param version a version range.
   * @return true if within range, false otherwise.
   */
  public boolean matches(@NonNull Version version) {
    return (version.isNewerThan(this.start) && version.isOlderThan(this.end)) || (
        version.equals(this.start) && this.startInclusive) || (version.equals(this.end)
        && this.endInclusive);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof VersionRange)) {
      return false;
    }
    VersionRange that = (VersionRange) o;
    return this.startInclusive == that.startInclusive &&
        this.endInclusive == that.endInclusive &&
        Objects.equals(this.start, that.start) &&
        Objects.equals(this.end, that.end);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.start, this.startInclusive, this.end, this.endInclusive);
  }
}
