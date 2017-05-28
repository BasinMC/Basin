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
package org.basinmc.faucet.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CapabilityInstance {

  /**
   * Get the type of capability this instance corresponds with
   */
  @Nonnull
  Capability getType();

  /**
   * Get the object holding this specific instance.
   */
  @Nonnull
  CapabilityHolder getHolder();

  /**
   * Get a list of parameters for this capability
   */
  @Nonnull
  Capability.Parameter[] getParameters();

  /**
   * Attempt to get a parameter with a string key
   *
   * @param key the key to look up with
   * @return a parameter matching the key, or null if none match
   * @throws UnsupportedOperationException if the capability doesn't support indexing parameters by
   * string key
   */
  @Nullable
  Capability.Parameter getParameter(String key) throws UnsupportedOperationException;

}
