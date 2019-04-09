/*
 * Copyright 2018 Johannes Donath  <johannesd@torchmind.com>
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

import org.basinmc.faucet.extension.manifest.ExtensionManifest
import org.basinmc.faucet.util.VersionRange
import java.util.*

/**
 * Represents a reference to one or more versions of a given extension.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
open class ExtensionReference(val identifier: String, val versionRange: VersionRange) {

  /**
   * Evaluates whether this reference matches the indicated extension.
   *
   * @param manifest an extension.
   * @return true if extension matches, false otherwise.
   */
  fun matches(manifest: ExtensionManifest): Boolean {
    return manifest.identifier.equals(this.identifier,
        ignoreCase = true) && manifest.version in this.versionRange
  }

  /**
   * {@inheritDoc}
   */
  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other !is ExtensionReference) {
      return false
    }
    val that = other as ExtensionReference?
    return this.identifier == that!!.identifier && this.versionRange == that.versionRange
  }

  /**
   * {@inheritDoc}
   */
  override fun hashCode(): Int {
    return Objects.hash(this.identifier, this.versionRange)
  }
}
