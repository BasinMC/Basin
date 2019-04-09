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
package org.basinmc.faucet.extension.registration

import org.basinmc.faucet.event.extension.ExtensionShutdownEvent
import org.basinmc.faucet.event.handler.Subscribe

/**
 * Provides a generic manager capable of handling registrations of arbitrary extensions.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
abstract class RegistrationManager<R : Registration> {

  /**
   * Retrieves a complete list of active registrations within this manager.
   *
   * @return a collection of registrations.
   */
  protected abstract val registrations: Collection<R>

  /**
   * Handles the graceful shutdown of an extension.
   *
   * @param event an event.
   */
  @Subscribe
  private fun handleExtensionShutdown(event: ExtensionShutdownEvent.Pre) {
    this.unregister({ reg -> reg.extension === event.extension })
  }

  /**
   * Appends a new registration to the manager.
   *
   * @param registration a registration.
   */
  protected abstract fun register(registration: R)

  /**
   * Appends multiple registrations to the manager.
   *
   * @param registrations a collection of registrations.
   */
  protected fun register(registrations: Iterable<R>) {
    registrations.forEach(this::register)
  }

  /**
   * Appends multiple registrations to the manager.
   *
   * @param registrations an array of registrations.
   */
  protected fun register(vararg registrations: R) {
    this.register(registrations.toList())
  }

  /**
   * Removes a single registration from the manager.
   *
   * @param registration a registration.
   */
  protected abstract fun unregister(registration: R)

  /**
   * Removes multiple registrations from the manager.
   *
   * @param registrations a collection of registrations.
   */
  protected fun unregister(registrations: Iterable<R>) {
    registrations.forEach(this::unregister)
  }

  /**
   * Removes multiple registrations from the manager.
   *
   * @param registrations an array of registrations.
   */
  protected fun unregister(vararg registrations: R) {
    this.unregister(registrations.toList())
  }

  /**
   * Removes all matching registrations from the manager.
   *
   * @param predicate a filter predicate.
   */
  protected fun unregister(predicate: (R) -> Boolean) {
    this.registrations.stream()
        .filter { predicate(it) }
        .forEach(this::unregister)
  }
}
