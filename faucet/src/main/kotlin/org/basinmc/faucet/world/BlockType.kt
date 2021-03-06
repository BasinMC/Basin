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
 * Represents a generic type of block.
 */
// TODO: Automatically generate from NMS code?
class BlockType(
    val fullBlock: Boolean,
    val translucent: Boolean,
    val tileEntity: Boolean,
    val randomTick: Boolean,
    val hardness: Float,
    val explosionResistance: Float,
    val slide: Float,
    val luminosity: Int,
    val name: String)
