/*
 * Copyright 2019 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package org.basinmc.faucet.event.filter;

import java.lang.annotation.Annotation;
import org.basinmc.faucet.event.Event;

/**
 * Provides arbitrary filter logic which evaluates whether a given event is to be handled by a bus
 * subscriber.
 *
 * Implementations of this type are generally expected to provide a no-args constructor with which
 * they are initialized when their associated annotation is encountered on a subscriber.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface EventFilter<A extends Annotation> {

  /**
   * Initializes the state of this particular filter.
   *
   * This method is guaranteed to be invoked <strong>before</strong> {@link #matches(Event)} is
   * invoked for the first time and effectively takes the place of the filter constructor (as this
   * retains the type safe aspects of the application).
   *
   * The implementation of this method is optional. By default no actions will be performed during
   * this stage.
   *
   * @param annotation an annotation instance from which this filter was initialized.
   */
  default void initialize(A annotation) {
  }

  /**
   * Evaluates whether this filter matches a given event instance.
   *
   * @param event an event.
   * @param <E> an event type.
   * @return true if matches, false otherwise.
   */
  <E extends Event<?>> boolean matches(E event);
}
