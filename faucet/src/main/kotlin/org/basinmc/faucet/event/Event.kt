/*
 * Copyright 2018 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.faucet.event

/**
 * Marks the annotated type as an event which may be passed to the local event bus.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
interface Event<out STATE> {

  /**
   * Retrieves the default state for this event's execution context.
   *
   * @return an arbitrary state.
   */
  val defaultState: STATE?
    get() = null
}