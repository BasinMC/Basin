/*
 * Copyright 2016 Hex <hex@hex.lc>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License&quot￼;
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
 *
 */
package org.basinmc.faucet.world

/**
 * Represents a biome type.
 */
// TODO: Original implementation was mutable - Probably for worldgen params? Restore functionality later
// TODO - auto-generate constants here from NMS code
enum class Biome(
    val temperature: Float,
    val rainfall: Float,

    // TODO figure out what these two do
    val baseHeight: Float,
    val heightVariation: Float)
