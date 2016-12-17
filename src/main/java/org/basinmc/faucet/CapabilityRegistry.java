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
package org.basinmc.faucet;

import javax.annotation.Nonnull;

/**
 * Interface for interaction with capabilities
 */
public interface CapabilityRegistry {
    /**
     * Register a new capability type. If a capability type with the same name and parameter types
     * already is registered, it will be returned instead.
     *
     * @param name a descriptive name for the capability
     * @param parameterTypes types the capability stores as parameters
     * @return a class descriptor
     */
    @Nonnull
    Class<? extends Capability> registerCapability(String name, Class<?>[] parameterTypes);

    /**
     * Checks if the given capability is registered.
     *
     * @param name a descriptive name for the capability
     * @param parameterTypes types the capability stores as parameters
     * @return true if the capability has been previously registered
     */
    boolean isRegistered(String name, Class<?>[] parameterTypes);
}
