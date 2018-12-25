/*
 * Copyright 2018 Johannes Donath  <johannesd@torchmind.com>
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
package org.basinmc.sink.plugin;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.jar.JarFile;
import org.basinmc.faucet.extension.Extension;
import org.basinmc.faucet.extension.dependency.ExtensionDependency;
import org.basinmc.faucet.extension.dependency.ServiceDependency;
import org.basinmc.faucet.extension.dependency.ServiceReference;
import org.basinmc.faucet.extension.error.ExtensionException;
import org.basinmc.faucet.extension.error.ExtensionManifestException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public class ExtensionImpl implements AutoCloseable, Extension {

  private final Path containerPath;
  private final JarFile jarFile;

  private final String identifier;
  private final String version;

  private Phase phase = Phase.REGISTERED;
  private AnnotationConfigApplicationContext ctx;

  ExtensionImpl(@NonNull Path containerPath) throws ExtensionException {
    this.containerPath = containerPath;

    try {
      this.jarFile = new JarFile(containerPath.toFile());

      var manifest = this.jarFile.getManifest();
      var attrs = manifest.getMainAttributes();

      this.identifier = attrs.getValue(IDENTIFIER_HEADER);
      this.version = attrs.getValue(VERSION_HEADER);

      if (this.identifier == null || this.identifier.isEmpty()) {
        throw new ExtensionManifestException("Missing extension identifier");
      }
      if (this.version == null || this.version.isEmpty()) {
        throw new ExtensionManifestException("Missing extension version");
      }
    } catch (IOException ex) {
      throw new ExtensionManifestException("Cannot access extension manifest", ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Optional<UUID> getDistributionId() {
    return Optional.empty(); // TODO: Unsupported atm
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
  public String getVersion() {
    return this.version;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Phase getPhase() {
    return this.phase;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<ServiceReference> getServices() {
    return Collections.emptyList(); // TODO: Scan and expose services
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<ExtensionDependency> getDependencies() {
    return Collections.emptyList(); // TODO: Parse dependencies
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<ServiceDependency> getServiceDependencies() {
    return Collections.emptyList(); // TODO: Discover service dependencies
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public String getDisplayName() {
    return this.getDisplayName(Locale.getDefault()); // TODO: Locale setting?
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public String getDisplayName(@NonNull Locale locale) {
    return ""; // TODO
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<String> getAuthors() {
    return Collections.emptyList(); // TODO
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<String> getContributors() {
    return Collections.emptyList(); // TODO
  }

  public void start(@NonNull ApplicationContext parentCtx) {
    if (this.ctx != null) {
      return;
    }

    this.ctx = new AnnotationConfigApplicationContext();
    this.ctx.setParent(parentCtx);
    // TODO

    this.phase = Phase.LOADED;
  }

  public void stop() {
    if (this.ctx == null) {
      return;
    }

    try {
      this.ctx.close();
    } finally {
      this.ctx = null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    try {
      this.stop();
    } finally {
      this.jarFile.close();
    }
  }
}
