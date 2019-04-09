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
package org.basinmc.faucet.extension.error

import org.basinmc.faucet.extension.Extension
import org.basinmc.faucet.extension.dependency.ExtensionDependency
import org.basinmc.faucet.extension.dependency.ServiceDependency
import org.basinmc.faucet.extension.manifest.ExtensionAuthor
import org.basinmc.faucet.extension.manifest.ExtensionManifest

/**
 * Notifies the caller about unresolved dependencies which prevent an extension from starting.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
class ExtensionResolverException : ExtensionContainerException {

  constructor(manifest: ExtensionManifest,
      extensionDependencies: List<ExtensionDependency>,
      serviceDependencies: List<ServiceDependency>) : super(
      buildErrorMessage(manifest, extensionDependencies, serviceDependencies))

  constructor(manifest: ExtensionManifest,
      extensions: List<Extension>) : super(buildErrorMessage(manifest, extensions))

  companion object {
    private fun buildErrorMessage(manifest: ExtensionManifest,
        extensionDependencies: List<ExtensionDependency>,
        serviceDependencies: List<ServiceDependency>): String {
      val builder = StringBuilder()
          .append("Cannot resolve one or more dependencies for extension")
          .append(manifest.identifier)
          .append(" v").append(manifest.version)
          .append(System.lineSeparator())
          .append(System.lineSeparator())

      appendExtensionMetadata(builder, manifest)

      if (!extensionDependencies.isEmpty()) {
        builder.append("=== Unresolved Extension Dependencies ===").append(System.lineSeparator())
        extensionDependencies.forEach { ext ->
          builder.append(" - ")
              .append(ext.identifier)
              .append(" v").append(ext.versionRange)
              .append(System.lineSeparator())
        }
        builder.append(System.lineSeparator())
      }

      if (!serviceDependencies.isEmpty()) {
        builder.append("=== Unresolved Service Dependencies ==").append(System.lineSeparator())
        serviceDependencies.forEach { srv ->
          builder.append(" - ")
              .append(srv.baseClassName)
              .append(" v").append(srv.versionRange)
              .append(System.lineSeparator())
        }
        builder.append(System.lineSeparator())
      }

      appendFooter(builder, manifest)

      return builder.toString()
    }

    private fun buildErrorMessage(manifest: ExtensionManifest,
        extensions: List<Extension>): String {
      val builder = StringBuilder()
          .append("Cannot resolve one or more dependencies for extension")
          .append(manifest.identifier)
          .append(" v").append(manifest.version)
          .append(System.lineSeparator())
          .append(System.lineSeparator())

      appendExtensionMetadata(builder, manifest)

      builder.append("=== Unresolved Extensions ===")
      extensions.forEach { e ->
        builder.append(" - ")
            .append(e.manifest.identifier)
            .append(" v").append(e.manifest.version)
            .append(System.lineSeparator())
      }
      builder.append(System.lineSeparator())

      appendFooter(builder, manifest)

      return builder.toString()
    }

    private fun appendExtensionMetadata(builder: StringBuilder, manifest: ExtensionManifest) {
      builder.append("=== Metadata ===").append(System.lineSeparator())
          .append("  Extension Id: ").append(manifest.identifier).append(System.lineSeparator())
          .append("  Version: ").append(manifest.version).append(System.lineSeparator())
          .append("  Display Name: ").append(manifest.displayName).append(System.lineSeparator())
          .append("  Author(s): ").append(
              manifest.authors
                  .map(ExtensionAuthor::toString)
                  .joinToString())
          .append(System.lineSeparator())
          .append("  Contributor(s): ").append(
              manifest.contributors
                  .map(ExtensionAuthor::toString)
                  .joinToString())
          .append(System.lineSeparator())
          .append(System.lineSeparator())
    }

    private fun appendFooter(builder: StringBuilder, manifest: ExtensionManifest) {
      // TODO: Refer to documentation link when available
      builder.append("This extension will not load until these dependencies are made available.")
          .append(System.lineSeparator())
      builder.append("Please refer to the extension documentation for more information")
    }
  }


}
