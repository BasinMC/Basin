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
import org.basinmc.faucet.event.extension.ExtensionRunEvent;
import org.basinmc.faucet.event.handler.Subscribe;
import org.basinmc.faucet.extension.Extension;

/**
 * Provides an extension to the registration manager specification which provides methods for
 * automatically scanning extension containers upon initialization.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class ScanningRegistrationManager<R extends Registration> extends
    RegistrationManager<R> {

  /**
   * Handles extension lifecycle events which indicate the switch into a run state.
   *
   * @param event an event.
   */
  @Subscribe
  private void handleExtensionRun(@NonNull ExtensionRunEvent.Post event) {
    this.scan(event.getExtension());
  }

  /**
   * Scans a single extension which entered the run state.
   *
   * @param extension an extension.
   * @return a collection of generated registrations.
   */
  protected abstract Collection<R> scan(@NonNull Extension extension);
}
