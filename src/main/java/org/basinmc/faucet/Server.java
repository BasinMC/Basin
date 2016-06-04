/*
 * Copyright 2016 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.basinmc.faucet;

import javax.annotation.Nonnull;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface Server {

    /**
     * Retrieves the currently active API version.
     *
     * @return an api version.
     */
    @Nonnull
    default String getApiVersion() {
        Package p = this.getClass().getPackage();

        if (p != null) {
            String specificationVersion = p.getSpecificationVersion();

            if (specificationVersion != null) {
                return specificationVersion;
            }
        }

        return "1.0.0-SNAPSHOT";
    }

    /**
     * Retrieves the currently active server version (as in game version such as 1.9.4 or 1.10).
     *
     * @return a server version.
     */
    @Nonnull
    String getVersion();

    /**
     * Checks whether the server authenticates against Mojang or whether any player can join
     * regardless of authentication.
     *
     * @return true if in online mode, false otherwise.
     */
    boolean isOnlineMode();
}
