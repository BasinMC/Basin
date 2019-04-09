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
package org.basinmc.faucet.util

import java.util.*

/**
 *
 * Represents a range which may match one or multiple versions.
 *
 *
 * Each range may consist of one or two versions in the format of `&lt;version&gt;[,&lt;version&gt;]` as well as a prefix and/or suffix which expresses whether a
 * given version is included or excluded from the range. Where:
 *
 *
 *  * `[` and `]` indicate that the start or end of the range is to be included
 *  * `(` and `)` indicate that the start or end of the range is to be excluded
 *
 *
 *
 * As such:
 *
 *
 *  * `1.0.0` includes 1.0.0
 *  * `[1.0.0` includes all versions newer than or equal to 1.0.0
 *  * `(1.0.0` includes all versions newer than 1.0.0
 *  * `1.0.0]` includes all versions older than or equal to 1.0.0
 *  * `1.0.0)` includes all versions older than 1.0.0
 *  * `[1.0.0,2.0.0)` includes all versions newer than or equal to 1.0.0 up to (excluding)
 * 2.0.0
 *
 *
 *
 * Note that none or only one prefix may be present when only a single version is specified
 * (e.g. `1.0.0`, `(1.0.0` and `1.0.0)` are valid while `(1.0.0]` is
 * considered invalid). When two versions are given, both prefixes must be present at the same time
 * to specify the bounds.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */

class VersionRange(range: String) {

  private val start: Version?
  private val startInclusive: Boolean
  private val end: Version?
  private val endInclusive: Boolean

  companion object {
    private val prefixCharacters = "(["
    private val suffixCharacters = "])"
  }

  init {
    var startString = range
    var endString = range

    val i = range.indexOf(',')
    if (i != -1) {
      startString = range.substring(0, i)
      endString = range.substring(i + 1)
    }

    val prefix = startString[0]
    val suffix = endString[endString.length - 1]

    val validPrefix = prefixCharacters.indexOf(prefix) != -1
    val validSuffix = suffixCharacters.indexOf(suffix) != -1

    if (validPrefix) {
      startString = startString.substring(1)
    }
    if (validSuffix) {
      endString = endString.substring(0, endString.length - 1)
    }

    if (i == -1) {
      if (validSuffix) {
        startString = startString.substring(0, startString.length - 1)
      }
      if (validPrefix) {
        endString = endString.substring(1)
      }
    }

    var start: Version?
    var end: Version?
    try {
      start = Version(startString)
    } catch (ex: IllegalArgumentException) {
      throw IllegalArgumentException("Illegal range start: $startString", ex)
    }

    if (i != -1) {
      try {
        end = Version(endString)
      } catch (ex: IllegalArgumentException) {
        throw IllegalArgumentException("Illegal range end: $endString", ex)
      }

    } else {
      end = start
    }

    if (i == -1) {
      if (validPrefix && validSuffix) {
        throw IllegalArgumentException(
            "Illegal range: \"$range\" specifies upper and lower bound with single version")
      }

      if (validPrefix) {
        end = null
      } else if (validSuffix) {
        start = null
      }
    } else if (!validPrefix || !validSuffix) {
      throw IllegalArgumentException(
          "Illegal range: \"$range\" must specify upper and lower bound")
    }

    this.start = start
    this.end = end
    this.startInclusive = i == -1 && !validPrefix && !validSuffix || validPrefix && prefix == '['
    this.endInclusive = i == -1 && !validPrefix && !validSuffix || validSuffix && suffix == ']'
  }

  /**
   * Evaluates whether a given version is part of this version range.
   *
   * @param version a version range.
   * @return true if within range, false otherwise.
   */
  operator fun contains(version: Version): Boolean {
    if (this.start != null && (this.start > version || !this.startInclusive && this.start == version)) {
      return false
    }
    return !(this.end != null && (this.end < version || !this.endInclusive && this.end == version))
  }

  override fun toString(): String {
    if (this.start === this.end) {
      return this.start!!.toString()
    }

    val builder = StringBuilder()
    if (this.start != null) {
      if (this.startInclusive) {
        builder.append('[')
      } else if (this.end != null) {
        builder.append('(')
      }

      builder.append(this.start)
    }
    if (this.end != null) {
      if (this.start != null) {
        builder.append(',')
      }

      builder.append(this.end)

      if (this.endInclusive) {
        builder.append(']')
      } else if (this.start != null) {
        builder.append(')')
      }
    }

    return builder.toString()
  }

  /**
   * {@inheritDoc}
   */
  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other !is VersionRange) {
      return false
    }
    val that = other as VersionRange?
    return this.startInclusive == that!!.startInclusive &&
        this.endInclusive == that.endInclusive &&
        this.start == that.start &&
        this.end == that.end
  }

  /**
   * {@inheritDoc}
   */
  override fun hashCode(): Int {
    return Objects.hash(this.start, this.startInclusive, this.end, this.endInclusive)
  }
}
