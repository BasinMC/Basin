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
 * <p>Represents a range which may match one or multiple versions.</p>
 *
 * <p>Each range may consist of one or two versions in the format of {@code
 * &lt;version&gt;[,&lt;version&gt;]} as well as a prefix and/or suffix which expresses whether a
 * given version is included or excluded from the range. Where:</p>
 *
 * <ul>
 * <li>{@code [} and {@code ]} indicate that the start or end of the range is to be included</li>
 * <li>{@code (} and {@code )} indicate that the start or end of the range is to be excluded</li>
 * </ul>
 *
 * <p>As such:</p>
 *
 * <ul>
 * <li>{@code 1.0.0} includes 1.0.0</li>
 * <li>{@code [1.0.0} includes all versions newer than or equal to 1.0.0</li>
 * <li>{@code (1.0.0} includes all versions newer than 1.0.0</li>
 * <li>{@code 1.0.0]} includes all versions older than or equal to 1.0.0</li>
 * <li>{@code 1.0.0)} includes all versions older than 1.0.0</li>
 * <li>{@code [1.0.0,2.0.0)} includes all versions newer than or equal to 1.0.0 up to (excluding)
 * 2.0.0</li>
 * </ul>
 *
 * <p>Note that none or only one prefix may be present when only a single version is specified
 * (e.g. {@code 1.0.0}, {@code (1.0.0} and {@code 1.0.0)} are valid while {@code (1.0.0]} is
 * considered invalid).</p>
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public class VersionRange {

  private static final String PREFIX_CHARS = "([";
  private static final String SUFFIX_CHARS = "])";

  private final Version start;
  private final boolean startInclusive;
  private final Version end;
  private final boolean endInclusive;

  /**
   * Parses a version range from its string representation.
   *
   * @param range an arbitrary range string.
   * @throws IllegalArgumentException when the specified range is partially or entirely invalid.
   */
  public VersionRange(@NonNull String range) {
    var startString = range;
    var endString = range;

    var i = range.indexOf(',');
    if (i != -1) {
      startString = range.substring(0, i);
      endString = range.substring(i + 1);
    }

    char prefix = startString.charAt(0);
    char suffix = endString.charAt(endString.length() - 1);

    boolean validPrefix = PREFIX_CHARS.indexOf(prefix) != -1;
    boolean validSuffix = SUFFIX_CHARS.indexOf(suffix) != -1;

    if (validPrefix) {
      startString = startString.substring(1);
    }
    if (validSuffix) {
      endString = endString.substring(0, endString.length() - 1);
    }

    if (i == -1) {
      if (validSuffix) {
        startString = startString.substring(0, startString.length() - 1);
      }
      if (validPrefix) {
        endString = endString.substring(1);
      }
    }

    Version start;
    Version end;
    try {
      start = new Version(startString);
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("Illegal range start: " + startString, ex);
    }
    if (i != -1) {
      try {
        end = new Version(endString);
      } catch (IllegalArgumentException ex) {
        throw new IllegalArgumentException("Illegal range end: " + endString, ex);
      }
    } else {
      end = start;
    }

    if (i == -1) {
      if (validPrefix && validSuffix) {
        throw new IllegalArgumentException(
            "Illegal range: \"" + range + "\" specifies upper and lower bound with single version");
      }

      if (validPrefix) {
        end = null;
      } else if (validSuffix) {
        start = null;
      }
    }

    this.start = start;
    this.end = end;
    this.startInclusive =
        (i == -1 && !validPrefix && !validSuffix) || (validPrefix && prefix == '[');
    this.endInclusive = (i == -1 && !validPrefix && !validSuffix) || (validSuffix && suffix == ']');
  }

  /**
   * Evaluates whether a given version is part of this version range.
   *
   * @param version a version range.
   * @return true if within range, false otherwise.
   */
  public boolean matches(@NonNull Version version) {
    if (this.start != null && (this.start.isNewerThan(version) || (!this.startInclusive
        && this.start.equals(version)))) {
      return false;
    }
    if (this.end != null && (this.end.isOlderThan(version) || (!this.endInclusive && this.end
        .equals(version)))) {
      return false;
    }

    return true;
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
