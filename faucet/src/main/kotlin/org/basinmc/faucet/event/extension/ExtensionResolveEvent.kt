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

import org.basinmc.faucet.event.StatelessEvent
import org.basinmc.faucet.extension.Extension
import org.basinmc.faucet.extension.Extension.Phase
import org.basinmc.faucet.util.BitMask

/**
 * Notifies listeners about an in-progress extension resolve.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
interface ExtensionResolveEvent<S> : ExtensionPhaseEvent<S> {

  /**
   * {@inheritDoc}
   */
  override val currentPhase: Phase
    get() = Phase.REGISTERED

  /**
   * {@inheritDoc}
   */
  override val targetPhase: Phase
    get() = Phase.RESOLVED

  class Pre @JvmOverloads constructor(extension: Extension, state: State = State.RESOLVE) :
      AbstractStatefulExtensionEvent<State>(extension, state), ExtensionResolveEvent<State>

  class Post(extension: Extension) : AbstractExtensionEvent<Unit>(extension),
      ExtensionResolveEvent<Unit>, StatelessEvent

  class State(mask: Int) : BitMask<State>(mask) {
    override val definition = Companion

    companion object : Definition<State> {

      val RESOLVE = State(1)

      override val values = listOf(RESOLVE)

      override fun newInstance(mask: Int) = State(mask)
    }
  }
}
