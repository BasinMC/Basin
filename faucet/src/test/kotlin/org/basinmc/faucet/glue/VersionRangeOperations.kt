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
import org.basinmc.faucet.util.VersionRange
import org.basinmc.faucet.world.VersionRangeWorld
import org.basinmc.faucet.world.VersionWorld
import org.junit.Assert.assertTrue
import org.springframework.beans.factory.annotation.Autowired

/**
 * Provides Cucumber operations related to the VersionRange object.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
class VersionRangeOperations {

  @Autowired
  private lateinit var world: VersionRangeWorld
  @Autowired
  private lateinit var versionWorld: VersionWorld

  @Before
  fun resetWorld() {
    this.world.reset()
  }

  @Given("""^a version range "([^"]+)"$""")
  fun createVersionRange(str: String) {
    this.world += VersionRange(str)
  }

  @Then("""^I expect the version to be part of this range$""")
  fun assertIn() {
    assertTrue(this.versionWorld.version in this.world.range)
  }

  @Then("""^I expect the version to not be part of this range$""")
  fun assertNotIn() {
    assertTrue(this.versionWorld.version !in this.world.range)
  }
}
