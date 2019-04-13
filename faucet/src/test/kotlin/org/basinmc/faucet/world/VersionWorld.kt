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
package org.basinmc.faucet.world

import org.basinmc.faucet.util.Version
import org.junit.Assert.assertTrue
import org.springframework.stereotype.Component

/**
 * Stores persistent cucumber test state for version related tests.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
@Component
class VersionWorld {

  val versions = mutableListOf<Version>()
  val version: Version
    get() {
      assertTrue("More than one version stashed by scenario", this.versions.size == 1)
      return this.versions.first()
    }

  val old: Version
    get() {
      assertTrue("Exactly two versions expected", this.versions.size == 2)
      return this.versions[0]
    }
  val new: Version
    get() {
      assertTrue("Exactly two versions expected", this.versions.size == 2)
      return this.versions[1]
    }

  fun reset() {
    this.versions.clear()
  }

  operator fun plusAssign(version: Version) {
    this.versions += version
  }
}
