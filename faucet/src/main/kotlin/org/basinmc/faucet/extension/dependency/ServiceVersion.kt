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
package org.basinmc.faucet.extension.dependency

import java.util.Objects
import org.basinmc.faucet.util.Version

/**
 * References a single service version.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
class ServiceVersion(val identifier: String, val version: Version) {

  /**
   * {@inheritDoc}
   */
  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other !is ServiceVersion) {
      return false
    }
    val that = other as ServiceVersion?
    return this.identifier == that!!.identifier && this.version == that.version
  }

  /**
   * {@inheritDoc}
   */
  override fun hashCode(): Int {
    return Objects.hash(this.identifier, this.version)
  }
}
