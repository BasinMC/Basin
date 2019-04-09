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
import java.util.regex.Pattern

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val extra: String?,
    val buildMetadata: String?,
    val stability: Stability,
    val stabilityIndex: Int = 0) : Comparable<Version> {

  companion object {

    /**
     * Pattern with which semantic versions may be validated or separated into their respective
     * components.
     */
    // 1  => Major
    // 2  => Major (non-zero)
    // 3  => Minor
    // 4  => Minor (non-zero)
    // 5  => Patch
    // 6  => Patch (non-zero)
    // 7  => Extra (with separator)
    // 8  => Extra
    // 9  => Metadata (with separator)
    // 10 => Metadata
    val pattern: Pattern = Pattern.compile(
        "^(([1-9][0-9]*)|0)\\.(([1-9][0-9]*)|0)\\.(([1-9][0-9]*)|0)(-([A-Z0-9-.]+))?(\\+([A-Z0-9-.]+))?$",
        Pattern.CASE_INSENSITIVE)

    /**
     * Represents the default version which may be substituted in cases where no specific version is
     * expected.
     */
    val default = Version(0, 0, 0, null, null, Stability.UNKNOWN, 0)

    operator fun invoke(version: String): Version {
      val matcher = pattern.matcher(version)
      if (!matcher.matches()) {
        throw IllegalArgumentException("Malformed version number: $version")
      }

      val major = try {
        Integer.parseUnsignedInt(matcher.group(1))
      } catch (ex: NumberFormatException) {
        throw IllegalArgumentException("Malformed major bit: " + matcher.group(1), ex)
      }

      val minor = try {
        Integer.parseUnsignedInt(matcher.group(3))
      } catch (ex: NumberFormatException) {
        throw IllegalArgumentException("Malformed minor bit: " + matcher.group(2), ex)
      }

      val patch = try {
        Integer.parseUnsignedInt(matcher.group(5))
      } catch (ex: NumberFormatException) {
        throw IllegalArgumentException("Malformed patch bit: " + matcher.group(3), ex)
      }

      val extra: String? = matcher.group(8)
      val buildMetadata: String? = matcher.group(10)

      val stability: Stability
      val stabilityIndex: Int

      if (extra == null) {
        stability = Stability.STABLE
        stabilityIndex = 0
      } else {
        val elements = extra.split(".")
            .dropLastWhile(String::isEmpty)

        stability = elements.firstOrNull()
            ?.let { Stability[it] } ?: Stability.UNKNOWN
        stabilityIndex = elements
            .takeIf { it.size >= 2 }
            ?.lastOrNull()
            ?.let {
              try {
                java.lang.Integer.parseInt(it)
              } catch (ex: NumberFormatException) {
                null
              }
            } ?: 0
      }

      return Version(major, minor, patch, extra, buildMetadata, stability, stabilityIndex)
    }
  }

  /**
   * {@inheritDoc}
   */
  override operator fun compareTo(other: Version): Int {
    if (this == other) {
      return 0
    }

    if (this.major > other.major) {
      return 1
    }
    if (this.major < other.major) {
      return -1
    }

    if (this.minor > other.minor) {
      return 1
    }
    if (this.minor < other.minor) {
      return -1
    }

    if (this.patch > other.patch) {
      return 1
    }
    if (this.patch < other.patch) {
      return -1
    }

    if (this.stability.ordinal > other.stability.ordinal) {
      return 1
    }
    if (this.stability.ordinal < other.stability.ordinal) {
      return -1
    }
    if (this.stabilityIndex > other.stabilityIndex) {
      return 1
    }

    return -1
  }

  /**
   * {@inheritDoc}
   */
  override fun toString(): String {
    var str = String.format("%d.%d.%d", this.major, this.minor, this.patch)
    if (this.extra != null) {
      str += "-" + this.extra
    }
    if (this.buildMetadata != null) {
      str += "+" + this.buildMetadata
    }
    return str
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Version) return false

    if (major != other.major) return false
    if (minor != other.minor) return false
    if (patch != other.patch) return false
    if (extra != other.extra) return false

    return true
  }

  override fun hashCode(): Int {
    var result = major
    result = 31 * result + minor
    result = 31 * result + patch
    result = 31 * result + (extra?.hashCode() ?: 0)
    return result
  }

  /**
   * Provides a list of valid version stability levels.
   */
  enum class Stability(vararg keywords: String) {

    ALPHA("a", "alpha"),
    BETA("b", "beta"),
    RELEASE_CANDIDATE("rc"),
    UNKNOWN,
    STABLE;

    private val keywords = keywords.toSet()

    companion object {

      private val keywordMap = HashMap<String, Stability>()

      init {
        for (stability in values()) {
          stability.keywords.forEach { k -> keywordMap[k] = stability }
        }
      }

      operator fun get(keyword: String): Stability? = keywordMap[keyword.toLowerCase()]
    }
  }
}
