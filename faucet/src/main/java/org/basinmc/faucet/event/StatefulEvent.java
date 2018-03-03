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
package org.basinmc.faucet.event;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.basinmc.faucet.util.State;

/**
 * Provides a base interface for "stateful" events (e.g. events which make use of the standard set
 * of states as declared by {@link State} in order to decide whether the action which initially
 * triggered the event posting is to be executed or not).
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface StatefulEvent extends MutableEvent {

  /**
   * Retrieves the events respective default state.
   *
   * Note: This method is guaranteed to return {@link State#ALLOW} or {@link State#DENY}.
   */
  @NonNull
  State getDefaultState();

  /**
   * Retrieves the current event state.
   *
   * Note: This method is guaranteed to return {@link State#ALLOW} or {@link State#DENY}.
   *
   * @see #getDefaultState() in order to retrieve the respective default value for this event.
   */
  @NonNull
  State getState();

  /**
   * Sets the current event state.
   *
   * Note: Setting the state of an event to {@link State#DEFAULT} will reset it to its respective
   * default value (as indicated by {@link #getDefaultState()}).
   *
   * @throws IllegalArgumentException when {@link State#WILDCARD} is being passed.
   * @throws IllegalStateException when the event state has been finalized.
   */
  void setState(@NonNull State state);
}
