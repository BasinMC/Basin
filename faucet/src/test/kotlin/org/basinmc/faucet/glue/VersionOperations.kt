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
package org.basinmc.faucet.glue

import cucumber.api.java.Before
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import org.basinmc.faucet.util.Version
import org.basinmc.faucet.world.VersionWorld
import org.junit.Assert.*
import org.springframework.beans.factory.annotation.Autowired

/**
 * Provides Cucumber operations related to the Version object.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
class VersionOperations {

  @Autowired
  private lateinit var world: VersionWorld

  @Before
  fun resetWorld() = this.world.reset()

  @Given("""^a version string "([^"]+)"$""")
  fun createVersion(str: String) {
    this.world += Version(str)
  }

  @Then("""^I expect the major bit to be "([^"]+)"$""")
  fun assertMajor(bit: Int) {
    assertEquals(bit, this.world.version.major)
  }

  @Then("""^minor to be "([^"]+)"$""")
  fun assertMinor(bit: Int) {
    assertEquals(bit, this.world.version.minor)
  }

  @Then("""^patch to be "([^"]+)"$""")
  fun assertPatch(bit: Int) {
    assertEquals(bit, this.world.version.patch)
  }

  @Then("""^extra to be "([^"]*)"$""")
  fun assertExtra(bit: String) {
    if (bit.isBlank()) {
      assertNull(this.world.version.extra)
    } else {
      assertEquals(bit, this.world.version.extra)
    }
  }

  @Then("""^metadata to be "([^"]*)"$""")
  fun assertMetadata(bit: String) {
    if (bit.isBlank()) {
      assertNull(this.world.version.buildMetadata)
    } else {
      assertEquals(bit, this.world.version.buildMetadata)
    }
  }

  @Then("""^it to be (.*)$""")
  fun assertStability(stability: Version.Stability) {
    assertEquals(stability, this.world.version.stability)
  }

  @Then("""^expect new to be more recent than old$""")
  fun assertMoreRecent() {
    assertTrue(this.world.new > this.world.old)
  }

  @Then("""^new to be less recent than old$""")
  fun assertLessRecent() {
    assertTrue(this.world.old < this.world.new)
  }
}
