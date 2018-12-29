/*
 * Copyright 2018 Johannes Donath  <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"),
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public class VersionTest {

  @Test
  public void testParse() {
    var version1 = new Version("0.0.0");
    var version2 = new Version("0.0.1");
    var version3 = new Version("0.1.0");
    var version4 = new Version("0.1.1");
    var version5 = new Version("1.0.0");
    var version6 = new Version("1.0.1");
    var version7 = new Version("1.1.0");
    var version8 = new Version("1.1.1");
    var version9 = new Version("1.1.1-alpha");
    var version10 = new Version("1.1.1-beta");
    var version11 = new Version("1.1.1-rc");
    var version12 = new Version("1.1.1-potato");
    var version13 = new Version("1.1.1+test");
    var version14 = new Version("1.1.1+abcdef");
    var version15 = new Version("1.1.1-alpha+abcdef");

    assertEquals(0, version1.major());
    assertEquals(0, version1.minor());
    assertEquals(0, version1.patch());
    assertFalse(version1.extra().isPresent());
    assertFalse(version1.buildMetadata().isPresent());

    assertEquals(0, version2.major());
    assertEquals(0, version2.minor());
    assertEquals(1, version2.patch());
    assertFalse(version2.extra().isPresent());
    assertFalse(version2.buildMetadata().isPresent());

    assertEquals(0, version3.major());
    assertEquals(1, version3.minor());
    assertEquals(0, version3.patch());
    assertFalse(version3.extra().isPresent());
    assertFalse(version3.buildMetadata().isPresent());

    assertEquals(0, version4.major());
    assertEquals(1, version4.minor());
    assertEquals(1, version4.patch());
    assertFalse(version4.extra().isPresent());
    assertFalse(version4.buildMetadata().isPresent());

    assertEquals(1, version5.major());
    assertEquals(0, version5.minor());
    assertEquals(0, version5.patch());
    assertFalse(version5.extra().isPresent());
    assertFalse(version5.buildMetadata().isPresent());

    assertEquals(1, version6.major());
    assertEquals(0, version6.minor());
    assertEquals(1, version6.patch());
    assertFalse(version6.extra().isPresent());
    assertFalse(version6.buildMetadata().isPresent());

    assertEquals(1, version7.major());
    assertEquals(1, version7.minor());
    assertEquals(0, version7.patch());
    assertFalse(version7.extra().isPresent());
    assertFalse(version7.buildMetadata().isPresent());

    assertEquals(1, version8.major());
    assertEquals(1, version8.minor());
    assertEquals(1, version8.patch());
    assertFalse(version8.extra().isPresent());
    assertFalse(version8.buildMetadata().isPresent());
  }

  @Test
  public void testCompare() {
    var versions = new Version[]{
        new Version("0.0.0"),
        new Version("0.0.1"),
        new Version("0.0.2"),
        new Version("0.1.0-alpha"),
        new Version("0.1.0-beta"),
        new Version("0.1.0"),
        new Version("0.1.1-alpha"),
        new Version("0.1.1-beta"),
        new Version("0.1.1"),
        new Version("0.2.0-alpha"),
        new Version("0.2.0-beta"),
        new Version("0.2.0"),
        new Version("1.0.0-alpha"),
        new Version("1.0.0-beta"),
        new Version("1.0.0"),
        new Version("1.0.1-alpha"),
        new Version("1.0.1-beta"),
        new Version("1.0.1"),
        new Version("1.1.0-alpha"),
        new Version("1.1.0-beta"),
        new Version("1.1.0"),
        new Version("2.0.0-alpha"),
        new Version("2.0.0-beta"),
        new Version("2.0.0"),
    };

    for (var i = 1; i < versions.length; ++i) {
      var version = versions[i];

      for (var j = 0; j < versions.length; ++j) {
        var other = versions[j];

        if (j < i) {
          assertTrue(version.isNewerThan(other), "version #" + i + " must be newer than #" + j);
          assertFalse(version.isOlderThan(other), "version #" + i + " cannot be older than #" + j);
          assertNotEquals(version, other, "version #" + i + " cannot be equal to #" + j);
          assertEquals(1, version.compareTo(other), "version #" + i + " must be larger than #" + j);
        } else if (j > i) {
          assertTrue(version.isOlderThan(other), "version #" + i + " must be older than #" + j);
          assertFalse(version.isNewerThan(other), "version #" + i + " cannot be newer than #" + j);
          assertNotEquals(version, other, "version #" + i + " cannot be equal to #" + j);
          assertEquals(-1, version.compareTo(other),
              "version #" + i + " must be larger than #" + j);
        } else {
          assertFalse(version.isNewerThan(other), "version #" + i + " cannot be newer than #" + j);
          assertFalse(version.isOlderThan(other), "version #" + i + " cannot be older than #" + j);
          assertEquals(version, other, "version #" + i + " must be equal to #" + j);
          assertEquals(0, version.compareTo(other), "version #" + i + " must be larger than #" + j);
        }
      }
    }
  }

  @Test
  public void testToString() {
    assertEquals("1.0.0", new Version("1.0.0").toString());
    assertEquals("1.0.1", new Version("1.0.1").toString());
    assertEquals("1.1.0", new Version("1.1.0").toString());
    assertEquals("2.0.0", new Version("2.0.0").toString());
  }
}
