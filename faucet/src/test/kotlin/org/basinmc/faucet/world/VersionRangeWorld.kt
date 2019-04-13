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

import org.basinmc.faucet.util.VersionRange
import org.springframework.stereotype.Component

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
@Component
class VersionRangeWorld {

  val ranges = mutableListOf<VersionRange>()
  val range: VersionRange
    get() = this.ranges.first()

  fun reset() {
    this.ranges.clear()
  }

  operator fun plusAssign(range: VersionRange) {
    this.ranges += range
  }
}
