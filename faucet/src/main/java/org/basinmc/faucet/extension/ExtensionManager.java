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
import java.util.List;

/**
 * Provides utility methods which permit the initialization of the Basin application and extension
 * context during the server startup.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface ExtensionManager {

  /**
   * Specifies the file extension which is expected to be present on all extension containers.
   */
  String CONTAINER_EXTENSION = ".bec";

  /**
   * Specifies the mime type which is typically transmitted for extension containers.
   */
  String CONTAINER_MIME = "application/basin-extension";

  /**
   * <p>Retrieves a list of extensions which have been registered with the server.</p>
   *
   * <p>Note that extensions may reside in different phases depending on when they are registered
   * with the server and whether their startup completed successfully (failed plugins may retain
   * their resolved state until removed from the server).</p>
   *
   * @return a list of registered extensions.
   */
  @NonNull
  List<Extension> getExtensions();
}
