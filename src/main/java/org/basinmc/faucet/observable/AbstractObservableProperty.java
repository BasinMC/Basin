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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnull;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class AbstractObservableProperty<T> implements ObservableProperty<T> {
    private final List<Observer<? super T>> observers = new CopyOnWriteArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@Nonnull Observer<? super T> observer) {
        this.observers.remove(observer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attach(@Nonnull Observer<? super T> observer) {
        this.observers.add(observer);
    }

    /**
     * Calls all registered observers effectively yielding their final decision on updating the
     * property's value.
     *
     * @param newValue a new value.
     * @return if true
     */
    protected boolean callObservers(@Nonnull T newValue) {
        T oldValue = this.get();
        return !this.observers.stream().filter((o) -> !o.change(this, oldValue, newValue)).findAny().isPresent();
    }
}
