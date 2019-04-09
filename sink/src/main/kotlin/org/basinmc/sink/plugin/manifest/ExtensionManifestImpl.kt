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
package org.basinmc.sink.plugin.manifest

import org.basinmc.chloramine.manifest.Manifest
import org.basinmc.chloramine.manifest.error.ManifestException
import org.basinmc.faucet.extension.dependency.ExtensionDependency
import org.basinmc.faucet.extension.dependency.ServiceDependency
import org.basinmc.faucet.extension.dependency.ServiceVersion
import org.basinmc.faucet.extension.error.ExtensionManifestException
import org.basinmc.faucet.extension.manifest.ExtensionFlags
import org.basinmc.faucet.extension.manifest.ExtensionManifest
import org.basinmc.faucet.util.Version
import org.basinmc.faucet.util.VersionRange
import java.io.IOException
import java.nio.channels.ReadableByteChannel
import java.util.*

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
class ExtensionManifestImpl(source: Manifest) : ExtensionManifest {

  override val formatVersion = source.metadata.formatVersion.toInt()

  override val flags = ExtensionFlags(source.flags)
  override val identifier = source.metadata.identifier
  override val version = Version(source.metadata.version)

  override val displayName = "ToDoToDoToDo" // TODO

  override val authors = source.metadata.authors
      .map(::ExtensionAuthorImpl)
      .toList()
  override val contributors = source.metadata.contributors
      .map(::ExtensionAuthorImpl)
      .toList()
  override val services = source.metadata.providedServices
      .map { service ->
        ServiceVersion(service.identifier,
            Version(service.version))
      } // TODO: Throw ExtensionManifestException
      .toList()
  override val extensionDependencies = source.metadata.extensionDependencies
      .map { dependency ->
        ExtensionDependency(dependency.identifier,
            VersionRange(dependency.versionRange),
            dependency.isOptional)
      } // TODO: Throw ExtensionManifestException
      .toList()
  override val serviceDependencies: List<ServiceDependency> = source.metadata.serviceDependencies
      .map { dependency ->
        ServiceDependency(dependency.identifier,
            VersionRange(dependency.versionRange),
            dependency.isOptional)
      }  // TODO: Throw ExtensionManifestException
      .toList()

  companion object {

    private fun readData(channel: ReadableByteChannel): Manifest {
      try {
        return Manifest.read(channel)
      } catch (ex: ManifestException) {
        throw ExtensionManifestException("Cannot decode manifest", ex)
      }

    }
  }

  @Throws(ExtensionManifestException::class, IOException::class)
  constructor(channel: ReadableByteChannel) : this(readData(channel))

  override fun getDisplayName(locale: Locale): String {
    return this.displayName // TODO
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ExtensionManifestImpl) return false

    if (identifier != other.identifier) return false
    if (version != other.version) return false

    return true
  }

  override fun hashCode(): Int {
    var result = identifier.hashCode()
    result = 31 * result + version.hashCode()
    return result
  }
}
