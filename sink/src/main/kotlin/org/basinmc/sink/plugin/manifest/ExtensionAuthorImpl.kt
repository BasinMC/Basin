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
package org.basinmc.sink.plugin.manifest

import org.basinmc.chloramine.manifest.metadata.Author
import org.basinmc.faucet.extension.manifest.ExtensionAuthor

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
class ExtensionAuthorImpl(source: Author) : ExtensionAuthor {

  override val name = source.name
  override val alias: String? = source.alias.orElse(null)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ExtensionAuthorImpl) return false

    if (name != other.name) return false
    if (alias != other.alias) return false

    return true
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + (alias?.hashCode() ?: 0)
    return result
  }

}
