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

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * An object that stores various capabilities something might have.
 */
public interface Capabilities {

  /**
   * Checks if this object possesses a capability.
   *
   * @param capability The capability to check
   * @return true if the capability is present
   */
  boolean hasCapability(Capability capability);

  /**
   * Gets a capability present in this object. Changes to it must be reflected in this object.
   *
   * @param type the capability type
   * @return a capability
   */
  @Nullable
  CapabilityInstance getCapability(Capability type);
}
