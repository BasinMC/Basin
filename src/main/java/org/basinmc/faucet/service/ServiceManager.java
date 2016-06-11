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

import org.basinmc.faucet.plugin.PluginContext;
import org.basinmc.faucet.util.Priority;

import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * Manages the lifecycle of services which can be directly injected into objects or newly
 * constructed classes.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface ServiceManager {

    /**
     * Attempts to find a registered service.
     *
     * @param serviceType a service type.
     * @param <T>         a service interface type.
     * @return a service reference or, if no service for the specified interface was found, an empty
     * optional.
     */
    @Nonnull
    <T> Optional<ServiceReference<T>> getReference(@Nonnull Class<T> serviceType);

    /**
     * Registers a new service.
     *
     * @param interfaceType  an interface type.
     * @param implementation a service implementation.
     * @param <I>            an interface type.
     * @return a registration descriptor.
     */
    @Nonnull
    default <I> ServiceRegistration<I> register(@Nonnull Class<I> interfaceType, @Nonnull I implementation) {
        return this.register(interfaceType, implementation, Priority.NORMAL);
    }

    /**
     * Registers a new service.
     *
     * @param interfaceType  an interface type.
     * @param implementation a service implementation.
     * @param priority       a service priority.
     * @param <I>            an interface type.
     * @return a registration descriptor.
     */
    @Nonnull
    <I> ServiceRegistration<I> register(@Nonnull Class<I> interfaceType, @Nonnull I implementation, @Nonnull Priority priority);
}
