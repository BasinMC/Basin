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
package org.basinmc.sink.plugin.manifest;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import org.basinmc.chloramine.manifest.Manifest;
import org.basinmc.chloramine.manifest.error.ManifestException;
import org.basinmc.faucet.extension.dependency.ExtensionDependency;
import org.basinmc.faucet.extension.dependency.ServiceDependency;
import org.basinmc.faucet.extension.dependency.ServiceVersion;
import org.basinmc.faucet.extension.error.ExtensionManifestException;
import org.basinmc.faucet.extension.manifest.ExtensionAuthor;
import org.basinmc.faucet.extension.manifest.ExtensionFlags;
import org.basinmc.faucet.extension.manifest.ExtensionManifest;
import org.basinmc.faucet.util.Version;
import org.basinmc.faucet.util.VersionRange;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class ExtensionManifestImpl implements ExtensionManifest {

  private final Manifest source;

  private final ExtensionFlags flags;
  private final Version version;

  private final List<ExtensionAuthorImpl> authors;
  private final List<ExtensionAuthorImpl> contributors;
  private final List<ServiceVersion> services;
  private final List<ExtensionDependency> extensionDependencies;
  private final List<ServiceDependency> serviceDependencies;

  public ExtensionManifestImpl(@NonNull ByteBuffer buffer) throws ExtensionManifestException {
    this(readData(buffer));
  }

  public ExtensionManifestImpl(@NonNull Manifest source) {
    this.source = source;

    this.flags = new ExtensionFlags(source.getFlags());
    this.version = new Version(source.getMetadata().getVersion());

    this.authors = source.getMetadata().getAuthors().stream()
        .map(ExtensionAuthorImpl::new)
        .collect(Collectors.toList());
    this.contributors = source.getMetadata().getContributors().stream()
        .map(ExtensionAuthorImpl::new)
        .collect(Collectors.toList());

    this.services = source.getMetadata().getProvidedServices().stream()
        .map(service -> new ServiceVersion(service.getIdentifier(),
            new Version(service.getVersion()))) // TODO: Throw ExtensionManifestException
        .collect(Collectors.toList());
    this.extensionDependencies = source.getMetadata().getExtensionDependencies().stream()
        .map(dependency -> new ExtensionDependency(dependency.getIdentifier(),
            new VersionRange(dependency.getVersionRange()),
            dependency.isOptional())) // TODO: Throw ExtensionManifestException
        .collect(Collectors.toList());
    this.serviceDependencies = source.getMetadata().getServiceDependencies().stream()
        .map(dependency -> new ServiceDependency(dependency.getIdentifier(),
            new VersionRange(dependency.getVersionRange()),
            dependency.isOptional()))  // TODO: Throw ExtensionManifestException
        .collect(Collectors.toList());
  }

  @NonNull
  private static Manifest readData(@NonNull ByteBuffer buffer) throws ExtensionManifestException {
    try {
      return new Manifest(buffer);
    } catch (ManifestException ex) {
      throw new ExtensionManifestException("Cannot decode manifest", ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFormatVersion() {
    return this.source.getMetadata().getFormatVersion();
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public ExtensionFlags getFlags() {
    return this.flags;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public String getIdentifier() {
    return this.source.getMetadata().getIdentifier();
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Version getVersion() {
    return this.version;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<ServiceVersion> getServices() {
    return Collections.unmodifiableList(this.services);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<ExtensionDependency> getExtensionDependencies() {
    return Collections.unmodifiableList(this.extensionDependencies);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<ServiceDependency> getServiceDependencies() {
    return Collections.unmodifiableList(this.serviceDependencies);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public String getDisplayName() {
    return "ToDoToDoToDo"; // TODO
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public String getDisplayName(@NonNull Locale locale) {
    return this.getDisplayName(); // TODO
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<ExtensionAuthor> getAuthors() {
    return Collections.unmodifiableList(this.authors);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<ExtensionAuthor> getContributors() {
    return Collections.unmodifiableList(this.contributors);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExtensionManifestImpl)) {
      return false;
    }
    ExtensionManifestImpl that = (ExtensionManifestImpl) o;
    return Objects.equals(this.source, that.source);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.source);
  }
}
