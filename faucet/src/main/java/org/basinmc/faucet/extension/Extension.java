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
package org.basinmc.faucet.extension;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.basinmc.faucet.extension.manifest.ExtensionManifest;

/**
 * Represents the metadata associated with a loaded or to-be-loaded extension.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public interface Extension {

  /**
   * Retrieves the extension manifest.
   *
   * @return a manifest.
   */
  @NonNull
  ExtensionManifest getManifest();

  /**
   * Retrieves the phase in which this extension currently resides.
   *
   * @return a phase.
   */
  @NonNull
  Phase getPhase();

  /**
   * Represents the phases which plugins may enter.
   */
  enum Phase {

    /**
     * Plugin has been registered with the server but has not yet been loaded or initialized.
     */
    REGISTERED,

    /**
     * Plugin dependencies have been resolved but it has yet to be loaded and initialized.
     */
    RESOLVED,

    /**
     * Plugin has been loaded (e.g. a class loader has been created) but not yet initialized.
     */
    LOADED,

    /**
     * Plugin is running (e.g. its classes have been registered and initialized (where desired)).
     */
    RUNNING,
  }
}
