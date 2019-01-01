/*
 * Copyright 2018 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.faucet.extension.manifest;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Optional;

/**
 * Represents an extension author or contributor who participated or is actively participating in
 * the development of an extension.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface ExtensionAuthor {

  /**
   * Retrieves the author's display name (either a real name or given alias).
   *
   * @return a name.
   */
  @NonNull
  String getName();

  /**
   * <p>Retrieves the author's alias.</p>
   *
   * <p>This field is only ever populated when an author chooses to use their real name instead of
   * their alias (and wish to include their alias in the manifest).</p>
   *
   * @return a given alias.
   */
  @NonNull
  Optional<String> getAlias();
}
