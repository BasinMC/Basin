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

import org.basinmc.faucet.extension.Extension
import org.basinmc.faucet.extension.Extension.Phase
import org.basinmc.faucet.util.BitMask

/**
 * Notifies listeners about an in-progress extension registration.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
interface ExtensionRegistrationEvent<S> : ExtensionPhaseEvent<S> {

  /**
   * {@inheritDoc}
   */
  override val currentPhase: Phase
    get() = Phase.NONE

  /**
   * {@inheritDoc}
   */
  override val targetPhase: Phase
    get() = Phase.REGISTERED

  class Pre constructor(extension: Extension, state: State = State.REGISTER) :
      AbstractStatefulExtensionEvent<State>(extension, state), ExtensionRegistrationEvent<State>

  class Post(extension: Extension) : AbstractExtensionEvent<Void>(extension),
      ExtensionRegistrationEvent<Void>

  class State(mask: Int) : BitMask<State>(mask) {

    /**
     * {@inheritDoc}
     */
    override fun createInstance(mask: Int): State {
      return State(mask)
    }

    /**
     * {@inheritDoc}
     */
    override fun values() = listOf(REGISTER)

    companion object {

      val REGISTER = State(1)
    }
  }
}
