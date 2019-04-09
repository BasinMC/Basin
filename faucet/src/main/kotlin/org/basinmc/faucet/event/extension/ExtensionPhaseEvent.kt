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
package org.basinmc.faucet.event.extension

import org.basinmc.faucet.extension.Extension.Phase

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
interface ExtensionPhaseEvent<S> : ExtensionEvent<S> {

  /**
   * Retrieves the phase in which the extension currently resides.
   *
   * @return an extension phase.
   */
  val currentPhase: Phase

  /**
   * Retrieves the phase to which the extension is currently transitioning.
   *
   * @return a target extension phase.
   */
  val targetPhase: Phase
}
