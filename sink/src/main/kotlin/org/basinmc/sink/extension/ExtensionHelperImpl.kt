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
package org.basinmc.sink.extension

import org.basinmc.faucet.extension.Extension
import org.basinmc.faucet.extension.ExtensionHelper
import org.springframework.stereotype.Service
import java.lang.StackWalker.Option
import kotlin.streams.toList

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
@Service
class ExtensionHelperImpl : ExtensionHelper {

  private val stackWalker = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)

  override val callingExtension: Extension?
    get() = this.extensionFrames.firstOrNull()
  override val firstCallingExtension: Extension?
    get() = this.extensionFrames.lastOrNull()

  private val extensionFrames: List<ExtensionImpl>
    get() = this.stackWalker
        .walk { stream ->
          stream
              .map { it.declaringClass.classLoader }
              .filter { it is ExtensionClassLoader }
              .map { it as ExtensionClassLoader }
              .map(ExtensionClassLoader::extension)
              .toList()
        }
}
