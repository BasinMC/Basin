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
package org.basinmc.faucet.event

/**
 * Provides an extension to the standard event type which does not contain any mutable output state.
 *
 * Note that events of this type will simply internally refer to [Unit] in order to represent their
 * state rather than defining no state at all.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
interface StatelessEvent : Event<Unit> {

  override val defaultState: Unit
    get() = Unit
}
