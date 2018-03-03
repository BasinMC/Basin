/*
 * Copyright 2017 Hex <hex@hex.lc>
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
package org.basinmc.faucet.command;

import java.util.Set;
import edu.umd.cs.findbugs.annotations.NonNull;

public interface CommandSpec {

  /**
   * Get the name of this command.
   */
  @NonNull
  String getName();

  /**
   * Get a set of aliases that can be used to refer to this command, or an empty set.
   */
  @NonNull
  Set<String> getAliases();

  /**
   * Get the bundle that provides this command. Will either be a plugin or the server.
   */
  @NonNull
  Module getOwner();
}
