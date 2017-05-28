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
 * Provides a base interface for observing value changes.
 *
 * @param <T> the property type.
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
@FunctionalInterface
public interface Observer<T> {

  /**
   * Handles the change to an observed property.
   *
   * @param observable the observed property.
   * @param oldValue the old value.
   * @param newValue a new value.
   * @return if true allows altering of the property value, if false retains the old value.
   */
  boolean change(@Nonnull ObservableProperty<? extends T> observable, T oldValue, T newValue);
}
