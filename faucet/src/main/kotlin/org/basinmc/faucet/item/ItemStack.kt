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
package org.basinmc.faucet.item

/**
 * Represents an arbitrarily sized stack of items.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
data class ItemStack(val item: Item, val amount: Int) {

  companion object {

    /**
     * Identifies the maximum permitted amount of items which may be stacked under normal
     * circumstances.
     *
     * Note that this value may be overridden by specific item types (e.g. an item may decide
     * whether it is stackable at all).
     */
    val maxSize = 64
  }
}
