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
package org.basinmc.faucet.extension.manifest

import org.basinmc.faucet.util.BitMask

/**
 * Provides a list of valid extension flags which express additional information about a build.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
class ExtensionFlags(mask: Int) : BitMask<ExtensionFlags>(mask) {

  /**
   * {@inheritDoc}
   */
  override fun createInstance(mask: Int): ExtensionFlags {
    return ExtensionFlags(mask)
  }

  /**
   * {@inheritDoc}
   */
  override fun values(): List<ExtensionFlags> {
    TODO("Not yet implemented")
  }

  companion object {

    // 1 - 8 are reserved
    val PRIVATE = ExtensionFlags(16)
    val COMMERCIAL = ExtensionFlags(32)
    // 64 is reserved
    val CI_BUILD = ExtensionFlags(128)
  }
}
