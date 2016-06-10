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
package org.basinmc.faucet.service;

import org.basinmc.faucet.util.Priority;

import javax.annotation.Nonnull;

/**
 * Represents a registered service.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface ServiceRegistration<T> extends ServiceReference<T> {

    /**
     * Retrieves the registration priority.
     *
     * @return the priority.
     */
    @Nonnull
    Priority getPriority();

    /**
     * Un-registers a service from the manager and thus makes it un-available for newly injected
     * members as well as observers.
     */
    void unregister();
}
