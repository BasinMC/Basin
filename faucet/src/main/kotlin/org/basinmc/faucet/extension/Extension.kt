/*
 * Copyright 2018 Johannes Donath  <johannesd@torchmind.com>
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
package org.basinmc.faucet.extension

import org.basinmc.faucet.extension.manifest.ExtensionManifest
import org.springframework.context.ApplicationContext

/**
 * Represents the metadata associated with a loaded or to-be-loaded extension.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
interface Extension : Comparable<Extension> {

  /**
   * Retrieves the extension manifest.
   *
   * @return a manifest.
   */
  val manifest: ExtensionManifest

  /**
   * Retrieves the phase in which this extension currently resides.
   *
   * @return a phase.
   */
  val phase: Phase

  /**
   * Retrieves the application context which manages all extension components throughout their
   * lifecycle.
   *
   * @return an application context.
   * @throws IllegalStateException when the extension is not currently running.
   */
  // TODO: Re-Evaluate this interface as it exposes extension state and could cause leaks when used
  //       incorrectly
  val context: ApplicationContext?

  /**
   * {@inheritDoc}
   */
  override fun compareTo(other: Extension): Int {
    val dependsOnLocal = other.manifest.extensionDependencies.stream()
        .anyMatch { dep -> dep.matches(this.manifest) }
    val dependsOnOther = this.manifest.extensionDependencies.stream()
        .anyMatch { dep -> dep.matches(other.manifest) }

    // circular dependency or no relation at all - don't care
    if (dependsOnLocal == dependsOnOther) {
      return 0
    }

    // other depends on us => us == higher priority
    return if (dependsOnLocal) {
      -1
    } else 1

    // we depend on other => us == lower priority
  }

  /**
   * Represents the phases which plugins may enter.
   */
  enum class Phase {

    /**
     * Placeholder phase for filtering purposes.
     */
    NONE,

    /**
     * Extension has been registered with the server but has not yet been loaded or initialized.
     */
    REGISTERED,

    /**
     * Extension dependencies have been resolved but it has yet to be loaded and initialized.
     */
    RESOLVED,

    /**
     * Extension has been loaded (e.g. a class loader has been created) but not yet initialized.
     */
    LOADED,

    /**
     * Extension is running (e.g. its classes have been registered and initialized (where
     * desired)).
     */
    RUNNING
  }
}
