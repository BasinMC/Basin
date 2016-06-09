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
package org.basinmc.faucet.observable;

import javax.annotation.Nonnull;

/**
 * Represents an obvervable value (e.g. a value that can be changed by third party code at any time
 * but notifies all of its watchers of such modification).
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @param <T> the property type.
 */
public interface ObservableProperty<T> {

    /**
     * Attaches a new observer to the property.
     *
     * @param observer an observer which will receive notifications of this property's changes.
     */
    void attach(@Nonnull Observer<? super T> observer);

    /**
     * Removes an observer from the property.
     *
     * @param observer an observer which is currently receiving notifications of this property's
     *                 changes.
     */
    void remove(@Nonnull Observer<? super T> observer);

    /**
     * Retrieves the current observable value.
     *
     * @return a value.
     */
    T get();

    /**
     * Sets the current observable value.
     *
     * @param value a value.
     * @return true if the value was changed, false otherwise.
     */
    boolean set(T value);
}
