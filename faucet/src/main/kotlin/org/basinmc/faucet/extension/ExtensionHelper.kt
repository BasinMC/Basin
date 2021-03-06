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
package org.basinmc.faucet.extension

/**
 * Provides helper methods for interacting with extensions.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
interface ExtensionHelper {

  /**
   *
   * Retrieves the calling extension.
   *
   *
   * This method will inspect the stack in order to find the closest call originating from an
   * extension. The call to this method itself will be ignored.
   *
   *
   * **Note:** This method may be rather slow as it needs to analyze the call stack
   * and should thus be avoided where possible.
   *
   * @return a calling extension or an empty optional.
   */
  val callingExtension: Extension?

  /**
   *
   * Retrieves the calling extension.
   *
   *
   * This method behaves equally to [.getCallingExtension] but will walk the call stack
   * in reverse (e.g. starting at the earliest possible point within the call chain) in order to
   * locate the original extension which initiated the call change.
   *
   *
   * **Note:** This method may be rather slow as it needs to analyze the call stack
   * and should thus be avoided where possible.
   *
   * @return a calling extension or an empty optional.
   */
  val firstCallingExtension: Extension?
}
