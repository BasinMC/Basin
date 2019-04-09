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

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
class VersionTest {

  @Test
  fun testParse() {
    val (major, minor, patch, extra, buildMetadata) = Version("0.0.0")
    val (major1, minor1, patch1, extra1, buildMetadata1) = Version("0.0.1")
    val (major2, minor2, patch2, extra2, buildMetadata2) = Version("0.1.0")
    val (major3, minor3, patch3, extra3, buildMetadata3) = Version("0.1.1")
    val (major4, minor4, patch4, extra4, buildMetadata4) = Version("1.0.0")
    val (major5, minor5, patch5, extra5, buildMetadata5) = Version("1.0.1")
    val (major6, minor6, patch6, extra6, buildMetadata6) = Version("1.1.0")
    val (major7, minor7, patch7, extra7, buildMetadata7) = Version("1.1.1")
    val (major8, minor8, patch8, extra8, buildMetadata8, stability, stabilityIndex) = Version(
        "1.1.1-alpha")
    val (major9, minor9, patch9, extra9, buildMetadata9, stability1, stabilityIndex1) = Version(
        "1.1.1-beta")
    val (major10, minor10, patch10, extra10, buildMetadata10, stability2, stabilityIndex2) = Version(
        "1.1.1-rc")
    val (major11, minor11, patch11, extra11, buildMetadata11, stability3, stabilityIndex3) = Version(
        "1.1.1-potato")
    val (major12, minor12, patch12, extra12, buildMetadata12, stability4, stabilityIndex4) = Version(
        "1.1.1+test")
    val (major13, minor13, patch13, extra13, buildMetadata13, stability5, stabilityIndex5) = Version(
        "1.1.1+abcdef")
    val (major14, minor14, patch14, extra14, buildMetadata14, stability6, stabilityIndex6) = Version(
        "1.1.1-alpha+abcdef")

    assertEquals(0, major)
    assertEquals(0, minor)
    assertEquals(0, patch)
    assertNull(extra)
    assertNull(buildMetadata)

    assertEquals(0, major1)
    assertEquals(0, minor1)
    assertEquals(1, patch1)
    assertNull(extra1)
    assertNull(buildMetadata1)

    assertEquals(0, major2)
    assertEquals(1, minor2)
    assertEquals(0, patch2)
    assertNull(extra2)
    assertNull(buildMetadata2)

    assertEquals(0, major3)
    assertEquals(1, minor3)
    assertEquals(1, patch3)
    assertNull(extra3)
    assertNull(buildMetadata3)

    assertEquals(1, major4)
    assertEquals(0, minor4)
    assertEquals(0, patch4)
    assertNull(extra4)
    assertNull(buildMetadata4)

    assertEquals(1, major5)
    assertEquals(0, minor5)
    assertEquals(1, patch5)
    assertNull(extra5)
    assertNull(buildMetadata5)

    assertEquals(1, major6)
    assertEquals(1, minor6)
    assertEquals(0, patch6)
    assertNull(extra6)
    assertNull(buildMetadata6)

    assertEquals(1, major7)
    assertEquals(1, minor7)
    assertEquals(1, patch7)
    assertNull(extra7)
    assertNull(buildMetadata7)
  }

  @Test
  fun testCompare() {
    val versions = arrayOf(Version("0.0.0"), Version("0.0.1"), Version("0.0.2"),
        Version("0.1.0-alpha"), Version("0.1.0-beta"), Version("0.1.0"), Version("0.1.1-alpha"),
        Version("0.1.1-beta"), Version("0.1.1"), Version("0.2.0-alpha"), Version("0.2.0-beta"),
        Version("0.2.0"), Version("1.0.0-alpha"), Version("1.0.0-beta"), Version("1.0.0"),
        Version("1.0.1-alpha"), Version("1.0.1-beta"), Version("1.0.1"), Version("1.1.0-alpha"),
        Version("1.1.0-beta"), Version("1.1.0"), Version("2.0.0-alpha"), Version("2.0.0-beta"),
        Version("2.0.0"))

    for (i in 1 until versions.size) {
      val version = versions[i]

      for (j in versions.indices) {
        val other = versions[j]

        if (j < i) {
          assertTrue(version > other, "version #$i must be newer than #$j")
          assertFalse(version < other, "version #$i cannot be older than #$j")
          assertNotEquals(version, other, "version #$i cannot be equal to #$j")
          assertEquals(1, version.compareTo(other), "version #$i must be larger than #$j")
        } else if (j > i) {
          assertTrue(version < other, "version #$i must be older than #$j")
          assertFalse(version > other, "version #$i cannot be newer than #$j")
          assertNotEquals(version, other, "version #$i cannot be equal to #$j")
          assertEquals(-1, version.compareTo(other),
              "version #$i must be larger than #$j")
        } else {
          assertFalse(version > other, "version #$i cannot be newer than #$j")
          assertFalse(version < other, "version #$i cannot be older than #$j")
          assertEquals(version, other, "version #$i must be equal to #$j")
          assertEquals(0, version.compareTo(other), "version #$i must be larger than #$j")
        }
      }
    }
  }

  @Test
  fun testToString() {
    assertEquals("1.0.0", Version("1.0.0").toString())
    assertEquals("1.0.1", Version("1.0.1").toString())
    assertEquals("1.1.0", Version("1.1.0").toString())
    assertEquals("2.0.0", Version("2.0.0").toString())
  }
}
