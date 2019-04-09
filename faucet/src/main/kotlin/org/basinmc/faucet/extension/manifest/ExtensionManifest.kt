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
package org.basinmc.faucet.extension.manifest

import java.util.Locale
import org.basinmc.faucet.extension.dependency.ExtensionDependency
import org.basinmc.faucet.extension.dependency.ServiceDependency
import org.basinmc.faucet.extension.dependency.ServiceVersion
import org.basinmc.faucet.util.Version

/**
 * Represents the extension's identifying information (such as identifier, version, display name,
 * authors, etc) as well as its dependencies and provided services.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
interface ExtensionManifest {

  /**
   *
   * Retrieves the manifest format version.
   *
   *
   * In cases where multiple revisions are supported by the server implementation, this value
   * may be useful in providing fallback functionality where necessary.
   *
   * @return a manifest version.
   */
  val formatVersion: Int

  /**
   * Retrieves a list of set extension flags.
   *
   * @return a list of flags.
   */
  val flags: ExtensionFlags

  /**
   *
   * Retrieves the globally unique identification for this extension.
   *
   *
   * This value is used to refer to this extension within manifests (for instance, when a
   * dependency is declared) and is expected to be globally unique (e.g. may only ever exist once
   * within the extension ecosystem).
   *
   *
   * The expected format for this particular field is equal to the Java package format (as
   * outlined in the Java specification and Oracle Code Style). For instance: `org.example.project.extension`
   *
   * @return a unique extension identifier.
   */
  val identifier: String

  /**
   *
   * Retrieves a human readable (yet machine parsable) representation of the extension's version
   * number.
   *
   *
   * This value is expected to follow the [Semantic Versioning](https://semver.org)
   * specification in order to permit automatic dependency resolving based on compatibility (e.g.
   * evaluate whether an alternative version is expected to be API compatible).
   *
   * @return a extension version.
   */
  val version: Version

  /**
   * Retrieves a list of services which are provided by this extension and are made available to
   * plugins which wish to consume them.
   *
   * @return a list of provided services and their versions.
   */
  val services: List<ServiceVersion>

  /**
   * Retrieves a list of extension level dependencies which are required to be present in order to
   * permit extension loading or alter the extension loading order if present.
   *
   * @return a list of dependencies.
   */
  val extensionDependencies: List<ExtensionDependency>

  /**
   * Retrieves a list of services which are required to be present in order to permit extension
   * loading or alter the extension loading order if present.
   *
   * @return a list of service dependencies.
   */
  val serviceDependencies: List<ServiceDependency>

  /**
   *
   * Retrieves a human readable name for this extension.
   *
   *
   * This method behaves the same way as [.getDisplayName] but will always choose
   * the current server locale as its display locale.
   *
   * @return a display name.
   * @see .getDisplayName
   */
  val displayName: String

  /**
   * Retrieves a list of authors who are actively involved with the development of this extension.
   *
   * @return a sorted list of authors.
   */
  val authors: List<ExtensionAuthor>

  /**
   * Retrieves a list of contributors who contributed to the extension's development at least once.
   *
   * @return a sorted list of contributors.
   */
  val contributors: List<ExtensionAuthor>

  /**
   *
   * Retrieves a human readable name for this extension (e.g. a project name or summary of the
   * provided functionality).
   *
   *
   * This value may be localized by the extension author in order to make their plugins more
   * accessible.
   *
   *
   * When no localization has been declared for the specified display locale, a standard
   * translation will be returned instead.
   *
   * @param locale an arbitrary display locale.
   * @return a localized name.
   */
  fun getDisplayName(locale: Locale): String

  companion object {

    /**
     * Defines a magic number with which the extension container header is introduced.
     */
    val MAGIC_NUMBER = 0x0DEBAC1E
  }
}
