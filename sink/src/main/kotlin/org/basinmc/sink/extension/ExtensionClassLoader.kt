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

import java.net.MalformedURLException
import java.net.URLClassLoader

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
class ExtensionClassLoader @Throws(MalformedURLException::class)
constructor(val extension: ExtensionImpl) :
    URLClassLoader(arrayOf(extension.containerPath.toUri().toURL()),
        ExtensionClassLoader::class.java.classLoader) {

  /**
   * {@inheritDoc}
   */
  @Throws(ClassNotFoundException::class)
  override fun findClass(name: String): Class<*>? = try {
    super.findClass(name)
  } catch (ex: ClassNotFoundException) {
    this.findDependencyClass(name) ?: throw ex
  }

  /**
   * Locates a class within the extension's resolved dependencies.
   *
   * @param name a class name.
   * @return a resolved class or, if none was found, an empty optional.
   */
  private fun findDependencyClass(name: String): Class<*>? = this.extension.resolvedDependencies
      .mapNotNull {
        val classLoader = it.classLoader ?: return@mapNotNull null

        try {
          classLoader.findClass(name)
        } catch (ex: ClassNotFoundException) {
          return@mapNotNull null
        }
      }
      .firstOrNull()
}
