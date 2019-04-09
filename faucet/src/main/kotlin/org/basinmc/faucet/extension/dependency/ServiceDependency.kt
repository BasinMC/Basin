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

import org.basinmc.faucet.util.VersionRange

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
class ServiceDependency(baseClassName: String, versionRange: VersionRange,
    val optional: Boolean) : ServiceReference(baseClassName, versionRange) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ServiceDependency) return false
    if (!super.equals(other)) return false

    if (optional != other.optional) return false

    return true
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + optional.hashCode()
    return result
  }
}
