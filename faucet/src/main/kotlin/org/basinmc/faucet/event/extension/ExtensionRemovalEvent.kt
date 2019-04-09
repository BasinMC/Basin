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

/**
 * Notifies listeners about the removal of an extension in an arbitrary phase.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
interface ExtensionRemovalEvent : ExtensionPhaseEvent<Void> {

  /**
   * {@inheritDoc}
   */
  override val currentPhase: Phase
    get() = this.extension.phase

  /**
   * {@inheritDoc}
   */
  override val targetPhase: Phase
    get() = Phase.NONE

  class Pre(extension: Extension) : AbstractExtensionEvent<Void>(extension), ExtensionRemovalEvent

  class Post(extension: Extension) : AbstractExtensionEvent<Void>(extension), ExtensionRemovalEvent
}
