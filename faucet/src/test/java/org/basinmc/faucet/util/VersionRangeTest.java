/*
 * Copyright 2018 Johannes Donath <johannesd@torchmind.com>
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class VersionRangeTest {

  @Test
  public void testMatch() {
    var range1 = new VersionRange("1.0.0");
    var range2 = new VersionRange("(1.0.0");
    var range3 = new VersionRange("[1.0.0");
    var range4 = new VersionRange("1.0.0)");
    var range5 = new VersionRange("1.0.0]");
    var range6 = new VersionRange("(1.0.0,2.0.0]");
    var range7 = new VersionRange("[1.0.0,2.0.0)");
    var range8 = new VersionRange("[1.0.0,2.0.0]");
    var range9 = new VersionRange("(1.0.0,2.0.0)");

    var version1 = new Version("0.9.0");
    var version2 = new Version("1.0.0");
    var version3 = new Version("1.5.0");
    var version4 = new Version("2.0.0");
    var version5 = new Version("2.1.0");
    var version6 = new Version("3.0.0");

    assertFalse(range1.matches(version1));
    assertTrue(range1.matches(version2));
    assertFalse(range1.matches(version3));
    assertFalse(range1.matches(version4));
    assertFalse(range1.matches(version5));
    assertFalse(range1.matches(version6));

    assertFalse(range2.matches(version1));
    assertFalse(range2.matches(version2));
    assertTrue(range2.matches(version3));
    assertTrue(range2.matches(version4));
    assertTrue(range2.matches(version5));
    assertTrue(range2.matches(version6));

    assertFalse(range3.matches(version1));
    assertTrue(range3.matches(version2));
    assertTrue(range3.matches(version3));
    assertTrue(range3.matches(version4));
    assertTrue(range3.matches(version5));
    assertTrue(range3.matches(version6));

    assertTrue(range4.matches(version1));
    assertFalse(range4.matches(version2));
    assertFalse(range4.matches(version3));
    assertFalse(range4.matches(version4));
    assertFalse(range4.matches(version5));
    assertFalse(range4.matches(version6));

    assertTrue(range5.matches(version1));
    assertTrue(range5.matches(version2));
    assertFalse(range5.matches(version3));
    assertFalse(range5.matches(version4));
    assertFalse(range5.matches(version5));
    assertFalse(range5.matches(version6));

    assertFalse(range6.matches(version1));
    assertFalse(range6.matches(version2));
    assertTrue(range6.matches(version3));
    assertTrue(range6.matches(version4));
    assertFalse(range6.matches(version5));
    assertFalse(range6.matches(version6));

    assertFalse(range7.matches(version1));
    assertTrue(range7.matches(version2));
    assertTrue(range7.matches(version3));
    assertFalse(range7.matches(version4));
    assertFalse(range7.matches(version5));
    assertFalse(range7.matches(version6));

    assertFalse(range8.matches(version1));
    assertTrue(range8.matches(version2));
    assertTrue(range8.matches(version3));
    assertTrue(range8.matches(version4));
    assertFalse(range8.matches(version5));
    assertFalse(range8.matches(version6));

    assertFalse(range9.matches(version1));
    assertFalse(range9.matches(version2));
    assertTrue(range9.matches(version3));
    assertFalse(range9.matches(version4));
    assertFalse(range9.matches(version5));
    assertFalse(range9.matches(version6));
  }

  @Test
  public void testMalformed() {
    assertThrows(IllegalArgumentException.class, () -> new VersionRange("(1.0.0)"));
    assertThrows(IllegalArgumentException.class, () -> new VersionRange("(1.0.0]"));
    assertThrows(IllegalArgumentException.class, () -> new VersionRange("[1.0.0)"));
    assertThrows(IllegalArgumentException.class, () -> new VersionRange("[1.0.0]"));
    assertThrows(IllegalArgumentException.class, () -> new VersionRange("1.0.0,2.0.0"));
    assertThrows(IllegalArgumentException.class, () -> new VersionRange("1.0.0,2.0.0]"));
    assertThrows(IllegalArgumentException.class, () -> new VersionRange("1.0.0,2.0.0)"));
    assertThrows(IllegalArgumentException.class, () -> new VersionRange("[1.0.0,2.0.0"));
    assertThrows(IllegalArgumentException.class, () -> new VersionRange("(1.0.0,2.0.0"));
  }

  @Test
  public void testToString() {
    assertEquals("1.0.0", new VersionRange("(1.0.0").toString());
    assertEquals("[1.0.0", new VersionRange("[1.0.0").toString());
    assertEquals("1.0.0", new VersionRange("1.0.0)").toString());
    assertEquals("1.0.0]", new VersionRange("1.0.0]").toString());
    assertEquals("(1.0.0,2.0.0]", new VersionRange("(1.0.0,2.0.0]").toString());
    assertEquals("[1.0.0,2.0.0)", new VersionRange("[1.0.0,2.0.0)").toString());
  }
}
