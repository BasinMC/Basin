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
package org.basinmc.faucet.util

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

import org.junit.jupiter.api.Test

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
class BitMaskTest {

  @Test
  fun testHas() {
    val mask = SampleBitMask.A
        .set(SampleBitMask.B)
        .set(SampleBitMask.C)

    assertTrue(mask.has(SampleBitMask.A))
    assertTrue(mask.has(SampleBitMask.B))
    assertTrue(mask.has(SampleBitMask.C))
    assertFalse(mask.has(SampleBitMask.D))

    assertTrue(mask.has(SampleBitMask.A.set(SampleBitMask.B)))
    assertTrue(mask.has(SampleBitMask.B.set(SampleBitMask.C)))
  }
}
