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
package org.basinmc.faucet.plugin;

import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Provides a plugin context which capable of wiring dependencies (e.g. being notified of resolved
 * dependencies before being initialized).
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface WiredPluginContext extends PluginContext {

    /**
     * Retrieves a list of wired dependencies.
     *
     * @return a set of dependencies.
     */
    @Nonnull
    Set<PluginContext> getWiredDependencies();

    /**
     * Wires a new dependency plugin context into the package.
     *
     * @param context a context.
     */
    void wire(@Nonnull PluginContext context);
}
