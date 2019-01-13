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
package org.basinmc.faucet.extension.registration;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Provides a generic manager capable of handling registrations of arbitrary extensions.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class RegistrationManager<R extends Registration> {

  /**
   * Retrieves a complete list of active registrations within this manager.
   *
   * @return a collection of registrations.
   */
  @NonNull
  protected abstract Collection<R> getRegistrations();

  /**
   * Appends a new registration to the manager.
   *
   * @param registration a registration.
   */
  protected abstract void register(@NonNull R registration);

  /**
   * Appends multiple registrations to the manager.
   *
   * @param registrations a collection of registrations.
   */
  protected void register(@NonNull Iterable<R> registrations) {
    registrations.forEach(this::register);
  }

  /**
   * Appends multiple registrations to the manager.
   *
   * @param registrations an array of registrations.
   */
  protected void register(@NonNull R... registrations) {
    this.register(Set.of(registrations));
  }

  /**
   * Removes a single registration from the manager.
   *
   * @param registration a registration.
   */
  protected abstract void unregister(@NonNull R registration);

  /**
   * Removes multiple registrations from the manager.
   *
   * @param registrations a collection of registrations.
   */
  protected void unregister(@NonNull Iterable<R> registrations) {
    registrations.forEach(this::unregister);
  }

  /**
   * Removes multiple registrations from the manager.
   *
   * @param registrations an array of registrations.
   */
  protected void unregister(@NonNull R... registrations) {
    this.unregister(Set.of(registrations));
  }

  /**
   * Removes all matching registrations from the manager.
   *
   * @param predicate a filter predicate.
   */
  protected void unregister(@NonNull Predicate<R> predicate) {
    this.getRegistrations().stream()
        .filter(predicate::test)
        .forEach(this::unregister);
  }

  // TODO: Handle extension unload
}
