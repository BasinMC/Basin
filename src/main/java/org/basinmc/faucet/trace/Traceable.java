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
package org.basinmc.faucet.trace;

import org.basinmc.faucet.internal.warn.Volatile;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

/**
 * Represents an object that has a distinct, traceable lifecycle.
 */
public interface Traceable {

    /**
     * Gets this object's tracing object if it is initialized, otherwise initialize it.
     *
     * @return a tracer
     */
    @Nonnull
    Tracer trace();

    /**
     * Perform a trace on this object asynchronously. The tracer will be initialized in the same
     * manner as if {@link #trace()} was called, but all existing nodes will be based on a
     * snapshot of the attached object taken at the moment this method is called. The tracer
     * will be initialized asynchronously, then the callback will be called. It will continue
     * to be updated asynchronously as the attached object is updated, and, as such, should be
     * synchronized before most use. Because of the asynchronous nature of this method, the
     * initialized tracer will <strong>not</strong> be cached, and will therefore result in a
     * new allocation for each call.
     *
     * <i>This should not be used as a catch-all performance solution. It has very specific
     * use cases and should be used carefully.</i>
     *
     * @param callback the callback to call upon initialization
     */
    @Volatile("Implementation is low-priority.")
    void parallelTrace(@Nonnull Consumer<Tracer> callback);
}
