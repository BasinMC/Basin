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
import edu.umd.cs.findbugs.annotations.Nullable;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.basinmc.faucet.extension.dependency.ExtensionDependency;
import org.basinmc.faucet.extension.dependency.ServiceDependency;
import org.basinmc.faucet.extension.dependency.ServiceVersion;
import org.basinmc.faucet.extension.error.ExtensionManifestException;
import org.basinmc.faucet.extension.manifest.ExtensionAuthor;
import org.basinmc.faucet.extension.manifest.ExtensionManifest;
import org.basinmc.faucet.util.Version;
import org.basinmc.faucet.util.VersionRange;
import org.basinmc.sink.util.BufferUtil;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class ExtensionManifestImpl implements ExtensionManifest {

  private final String identifier;
  private final Version version;
  private final UUID distributionId;
  private final List<ExtensionAuthorImpl> authors = new ArrayList<>();
  private final List<ExtensionAuthorImpl> contributors = new ArrayList<>();
  private final List<ServiceVersion> services = new ArrayList<>();
  private final List<ExtensionDependency> extensionDependencies = new ArrayList<>();
  private final List<ServiceDependency> serviceDependencies = new ArrayList<>();

  private ExtensionManifestImpl(@NonNull ByteBuf buffer) throws ExtensionManifestException {
    BufferUtil.checkMagicValue(buffer, MAGIC_NUMBER,
        () -> new ExtensionManifestException("Illegal extension header"));
    // TODO: Header version byte

    this.identifier = BufferUtil.readString(buffer)
        .orElseThrow(() -> new ExtensionManifestException("Identifier must be specified"));
    this.version = BufferUtil.readString(buffer)
        .map(Version::new) // TODO: May throw IllegalArgumentException
        .orElseThrow(() -> new ExtensionManifestException("Version must be specified"));
    this.distributionId = BufferUtil.readUUID(buffer)
        .orElse(null);

    BufferUtil.readList(buffer, () -> this.authors, ExtensionAuthorImpl::new);
    BufferUtil.readList(buffer, () -> this.contributors, ExtensionAuthorImpl::new);

    BufferUtil.readList(buffer, () -> this.services, (buf) -> {
      var identifier = BufferUtil.readString(buf)
          .orElseThrow(() -> new IllegalArgumentException("Service must define identifier"));
      var version = BufferUtil.readString(buf)
          .map(Version::new)
          .orElseThrow(() -> new IllegalArgumentException("Service must define version"));

      return new ServiceVersion(identifier, version);
    });

    BufferUtil.readList(
        buffer,
        () -> this.extensionDependencies,
        readDependency(ExtensionDependency::new)
    );
    BufferUtil.readList(
        buffer,
        () -> this.serviceDependencies,
        readDependency(ServiceDependency::new)
    );
  }

  public ExtensionManifestImpl(
      @NonNull String identifier,
      @NonNull Version version,
      @Nullable UUID distributionId,
      @NonNull Collection<ExtensionAuthorImpl> authors,
      @NonNull Collection<ExtensionAuthorImpl> contributors,
      @NonNull Collection<ServiceVersion> services,
      @NonNull Collection<ExtensionDependency> extensionDependencies,
      @NonNull Collection<ServiceDependency> serviceDependencies) {
    this.identifier = identifier;
    this.version = version;
    this.distributionId = distributionId;
    this.services.addAll(services);
    this.extensionDependencies.addAll(extensionDependencies);
    this.serviceDependencies.addAll(serviceDependencies);
    this.authors.addAll(authors);
    this.contributors.addAll(contributors);
  }

  @NonNull
  private static <D> Function<ByteBuf, D> readDependency(@NonNull DependencyFactory<D> factory) {
    return (in) -> {
      var identifier = BufferUtil.readString(in)
          .orElseThrow(() -> new IllegalArgumentException("Dependency must define identifier"));
      var version = BufferUtil.readString(in)
          .map(VersionRange::new)
          .orElseThrow(() -> new IllegalArgumentException("Dependency must define version range"));
      var optional = in.readBoolean(); // TODO: Replace with bitmask

      return factory.create(identifier, version, optional);
    };
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Optional<UUID> getDistributionId() {
    return Optional.ofNullable(this.distributionId);
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public String getIdentifier() {
    return this.identifier;
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
    return Objects.equals(this.identifier, that.identifier) &&
        Objects.equals(this.version, that.version);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.identifier, this.version);
  }

  @FunctionalInterface
  private interface DependencyFactory<D> {

    @NonNull
    D create(@NonNull String identifier, @NonNull VersionRange version, boolean optional);
  }
}
