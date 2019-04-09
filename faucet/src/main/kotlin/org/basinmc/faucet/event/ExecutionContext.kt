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
package org.basinmc.faucet.event

import org.basinmc.faucet.util.State

/**
 * Provides a context to event handlers which wish to alter the event outcome (where applicable).
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
interface ExecutionContext<STATE : Enum<STATE>> {

  /**
   * Retrieves the events respective default state.
   *
   * Note: This method is guaranteed to return [State.ALLOW] or [State.DENY].
   */
  val defaultState: STATE

  /**
   * Retrieves the current event state.
   *
   * Note: This method is guaranteed to return [State.ALLOW] or [State.DENY].
   *
   * @see .getDefaultState
   */
  /**
   * Sets the current event state.
   *
   * Note: Setting the state of an event to [State.DEFAULT] will reset it to its respective
   * default value (as indicated by [.getDefaultState]).
   *
   * @throws IllegalArgumentException when [State.WILDCARD] is being passed.
   * @throws IllegalStateException when the event state has been finalized.
   */
  var state: STATE
}
