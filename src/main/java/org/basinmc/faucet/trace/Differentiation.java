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

import java.util.function.Function;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

/**
 * Represents a difference report between two objects of same or different types.
 *
 * @param <S> the type of the <strong>s</strong>tarting object
 * @param <F> the type of the <strong>f</strong>inishing object
 */
public interface Differentiation<S, F> {

    /**
     * Get a transformer function representing the change in a field with a given index.
     *
     * @param fieldIndex the index at which to differentiate
     * @param <I> the type of the field
     * @return a transformer function
     */
    @Nullable
    <I> Function<I, I> getDifference(@Nonnegative int fieldIndex);
}
