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

import javax.annotation.Nonnull;

/**
 * Denotes a type as having clearly defined data parameters which can be compared with another
 * object of the same or derived type to produce a differentiation result.
 *
 * @param <T> The type with which it is differentiable
 */
public interface Differentiable<T> {

    /**
     * Differentiate between the two objects, creating a difference report
     *
     * @param other the object to compare
     * @param <F> the type of the object
     * @return a difference report
     */
    @Nonnull
    <F> Differentiation<T, F> differentiate(F other);

    /**
     * Reverse all operations specified by the difference report, applying them to this object.
     *
     * @param difference the difference report
     */
    void reset(Differentiation<T, ?> difference);
}
