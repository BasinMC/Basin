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
package org.basinmc.faucet.exception;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import javax.annotation.Nonnull;

/**
 * Provides a base exception for all issues which arise within a plugin (or other bundle) while
 * being called inside of Faucet or one of its implementations.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class PluginException extends Exception {
    private final Bundle bnd;

    public PluginException(@Nonnull Bundle bnd) {
        super();
        this.bnd = bnd;
    }

    public PluginException(@Nonnull Bundle bnd, String s) {
        super(s);
        this.bnd = bnd;
    }

    public PluginException(@Nonnull Bundle bnd, String s, Throwable throwable) {
        super(s, throwable);
        this.bnd = bnd;
    }

    public PluginException(@Nonnull Bundle bnd, Throwable throwable) {
        super(throwable);
        this.bnd = bnd;
    }

    /**
     * Retrieves the offending bundle.
     */
    @Nonnull
    public Bundle getBundle() {
        return this.bnd;
    }
}
