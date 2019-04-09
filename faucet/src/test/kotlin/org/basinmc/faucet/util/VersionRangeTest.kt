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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Test

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
class VersionRangeTest {

  @Test
  fun testMatch() {
    val range1 = VersionRange("1.0.0")
    val range2 = VersionRange("(1.0.0")
    val range3 = VersionRange("[1.0.0")
    val range4 = VersionRange("1.0.0)")
    val range5 = VersionRange("1.0.0]")
    val range6 = VersionRange("(1.0.0,2.0.0]")
    val range7 = VersionRange("[1.0.0,2.0.0)")
    val range8 = VersionRange("[1.0.0,2.0.0]")
    val range9 = VersionRange("(1.0.0,2.0.0)")

    val version1 = Version("0.9.0")
    val version2 = Version("1.0.0")
    val version3 = Version("1.5.0")
    val version4 = Version("2.0.0")
    val version5 = Version("2.1.0")
    val version6 = Version("3.0.0")

    assertFalse(version1 in range1)
    assertTrue(version2 in range1)
    assertFalse(version3 in range1)
    assertFalse(version4 in range1)
    assertFalse(version5 in range1)
    assertFalse(version6 in range1)

    assertFalse(version1 in range2)
    assertFalse(version2 in range2)
    assertTrue(version3 in range2)
    assertTrue(version4 in range2)
    assertTrue(version5 in range2)
    assertTrue(version6 in range2)

    assertFalse(version1 in range3)
    assertTrue(version2 in range3)
    assertTrue(version3 in range3)
    assertTrue(version4 in range3)
    assertTrue(version5 in range3)
    assertTrue(version6 in range3)

    assertTrue(version1 in range4)
    assertFalse(version2 in range4)
    assertFalse(version3 in range4)
    assertFalse(version4 in range4)
    assertFalse(version5 in range4)
    assertFalse(version6 in range4)

    assertTrue(version1 in range5)
    assertTrue(version2 in range5)
    assertFalse(version3 in range5)
    assertFalse(version4 in range5)
    assertFalse(version5 in range5)
    assertFalse(version6 in range5)

    assertFalse(version1 in range6)
    assertFalse(version2 in range6)
    assertTrue(version3 in range6)
    assertTrue(version4 in range6)
    assertFalse(version5 in range6)
    assertFalse(version6 in range6)

    assertFalse(version1 in range7)
    assertTrue(version2 in range7)
    assertTrue(version3 in range7)
    assertFalse(version4 in range7)
    assertFalse(version5 in range7)
    assertFalse(version6 in range7)

    assertFalse(version1 in range8)
    assertTrue(version2 in range8)
    assertTrue(version3 in range8)
    assertTrue(version4 in range8)
    assertFalse(version5 in range8)
    assertFalse(version6 in range8)

    assertFalse(version1 in range9)
    assertFalse(version2 in range9)
    assertTrue(version3 in range9)
    assertFalse(version4 in range9)
    assertFalse(version5 in range9)
    assertFalse(version6 in range9)
  }

  @Test
  fun testMalformed() {
    assertThrows(IllegalArgumentException::class.java) { VersionRange("(1.0.0)") }
    assertThrows(IllegalArgumentException::class.java) { VersionRange("(1.0.0]") }
    assertThrows(IllegalArgumentException::class.java) { VersionRange("[1.0.0)") }
    assertThrows(IllegalArgumentException::class.java) { VersionRange("[1.0.0]") }
    assertThrows(IllegalArgumentException::class.java) { VersionRange("1.0.0,2.0.0") }
    assertThrows(IllegalArgumentException::class.java) { VersionRange("1.0.0,2.0.0]") }
    assertThrows(IllegalArgumentException::class.java) { VersionRange("1.0.0,2.0.0)") }
    assertThrows(IllegalArgumentException::class.java) { VersionRange("[1.0.0,2.0.0") }
    assertThrows(IllegalArgumentException::class.java) { VersionRange("(1.0.0,2.0.0") }
  }

  @Test
  fun testToString() {
    assertEquals("1.0.0", VersionRange("(1.0.0").toString())
    assertEquals("[1.0.0", VersionRange("[1.0.0").toString())
    assertEquals("1.0.0", VersionRange("1.0.0)").toString())
    assertEquals("1.0.0]", VersionRange("1.0.0]").toString())
    assertEquals("(1.0.0,2.0.0]", VersionRange("(1.0.0,2.0.0]").toString())
    assertEquals("[1.0.0,2.0.0)", VersionRange("[1.0.0,2.0.0)").toString())
  }
}
