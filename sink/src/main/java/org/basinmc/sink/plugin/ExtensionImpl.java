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
import edu.umd.cs.findbugs.annotations.Nullable;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.extension.Extension;
import org.basinmc.faucet.extension.dependency.ExtensionDependency;
import org.basinmc.faucet.extension.dependency.ServiceDependency;
import org.basinmc.faucet.extension.error.ExtensionAccessException;
import org.basinmc.faucet.extension.error.ExtensionContainerException;
import org.basinmc.faucet.extension.error.ExtensionException;
import org.basinmc.faucet.extension.error.ExtensionManifestException;
import org.basinmc.faucet.extension.error.ExtensionResolverException;
import org.basinmc.sink.plugin.manifest.ExtensionHeader;
import org.basinmc.sink.plugin.manifest.ExtensionManifestImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public class ExtensionImpl implements AutoCloseable, Extension {

  private final Path containerPath;
  private final Logger logger;

  private final ExtensionHeader header;
  private final ExtensionManifestImpl manifest;

  private Phase phase = Phase.REGISTERED;
  private final List<ExtensionImpl> resolvedDependencies = new ArrayList<>();
  private final Map<ExtensionImpl, ExtensionDependency> resolvedDependencySources = new HashMap<>();

  private ExtensionClassLoader classLoader;
  private AnnotationConfigApplicationContext ctx;

  ExtensionImpl(@NonNull Path containerPath) throws ExtensionException {
    this.containerPath = containerPath;

    try (var channel = FileChannel.open(containerPath, StandardOpenOption.READ)) {
      if (channel.size() < ExtensionHeader.LENGTH) {
        throw new ExtensionManifestException(
            "Missing or malformed extension header: Expected " + ExtensionHeader.LENGTH
                + " bytes but got " + channel.size());
      }

      var headerBuffer = ByteBuffer.allocate(ExtensionHeader.LENGTH);
      channel.read(headerBuffer);
      headerBuffer.flip();

      this.header = new ExtensionHeader(Unpooled.wrappedBuffer(headerBuffer));

      if (this.header.getSignatureLength() != 0) {
        // TODO
        throw new UnsupportedOperationException("Extension signature not yet supported");
      }

      if (this.header.getManifestLength() == 0) {
        throw new ExtensionManifestException(
            "Malformed extension header: Metadata must be present");
      }
      // TODO: Set reasonable size bounds
      if (this.header.getManifestLength() > Integer.MAX_VALUE) {
        throw new ExtensionManifestException(
            "Malformed extension header: Metadata exceeds " + Integer.MAX_VALUE + " bytes");
      }
      var manifestBuffer = ByteBuffer.allocate((int) this.header.getManifestLength());
      channel.read(manifestBuffer);
      manifestBuffer.flip();

      this.manifest = new ExtensionManifestImpl(Unpooled.wrappedBuffer(manifestBuffer));
    } catch (IOException ex) {
      throw new ExtensionAccessException("Cannot read container file", ex);
    }

    this.logger = LogManager.getFormatterLogger(this.manifest.getDisplayName());
  }

  /**
   * Retrieves the path to the container file from which this extension definition originates.
   *
   * @return a container file path.
   */
  @NonNull
  public Path getContainerPath() {
    return this.containerPath;
  }

  /**
   * Retrieves the container header.
   *
   * @return a container header.
   */
  @NonNull
  public ExtensionHeader getHeader() {
    return this.header;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public ExtensionManifestImpl getManifest() {
    return this.manifest;
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
   * Retrieves a list of dependencies which have already been resolved.
   *
   * @return a list of extensions.
   */
  @NonNull
  public List<ExtensionImpl> getResolvedDependencies() {
    return Collections.unmodifiableList(this.resolvedDependencies);
  }

  /**
   * <p>Retrieves the class loader with which this extension is loading its classes and
   * resources.</p>
   *
   * <p>If the plugin is not currently loaded or running, an empty optional will be returned
   * instead.</p>
   *
   * @return an extension class loader.
   */
  @NonNull
  public Optional<ExtensionClassLoader> getClassLoader() {
    return Optional.ofNullable(this.classLoader);
  }

  /**
   * <p>Retrieves the context in which this extension is <strong>currently</strong> executed.</p>
   *
   * <p>If the plugin is not in the running phase, an empty optional will be returned instead as no
   * context is present at the moment.</p>
   *
   * @return an application context.
   */
  @NonNull
  public Optional<AnnotationConfigApplicationContext> getContext() {
    return Optional.ofNullable(this.ctx);
  }

  /**
   * Wires a dependency into this extension.
   *
   * @param extension an extension.
   * @param dependency the dependency which is fulfilled by this wiring (if any).
   * @throws IllegalStateException when invoked outside of the resolve (e.g. {@link Phase#LOADED}
   * phase).
   */
  public void wireDependency(@NonNull ExtensionImpl extension,
      @Nullable ExtensionDependency dependency) {
    if (this.phase != Phase.REGISTERED) {
      throw new IllegalStateException("Cannot wire dependency in " + this.phase + " phase");
    }

    this.resolvedDependencies.add(extension);

    if (dependency != null) {
      this.resolvedDependencySources.put(extension, dependency);
    }
  }

  public void resolve() throws ExtensionContainerException {
    var unresolvedExtensions = this.manifest.getExtensionDependencies().stream()
        .filter((dep) -> !dep.isOptional())
        .filter((dep) -> this.resolvedDependencies.stream()
            .map(ExtensionImpl::getManifest).noneMatch(dep::matches))
        .collect(Collectors.toList());
    // TODO: Resolve services
    var unresolvedServices = Collections.<ServiceDependency>emptyList();

    if (!unresolvedExtensions.isEmpty() || !unresolvedServices.isEmpty()) {
      throw new ExtensionResolverException(this.manifest, unresolvedExtensions,
          unresolvedServices);
    }

    this.phase = Phase.RESOLVED;
  }

  /**
   * Initializes the extension class loader along with all of its dependencies.
   *
   * @throws ExtensionContainerException when the container cannot be accessed.
   */
  public void initialize() throws ExtensionContainerException {
    if (this.ctx != null) {
      return;
    }

    var unresolvedExtensions = this.resolvedDependencies.stream()
        .filter((e) -> e.phase != Phase.LOADED)
        .collect(Collectors.toList());

    unresolvedExtensions.stream()
        .filter((e) -> {
          var source = this.resolvedDependencySources.get(e);
          return source != null && source.isOptional();
        })
        .forEach((e) -> {
          this.logger
              .debug("Removed unresolved optional dependency to %s v%s", e.manifest.getIdentifier(),
                  e.manifest.getVersion());
          this.resolvedDependencies.remove(e);
          this.resolvedDependencySources.remove(e);
        });
    var requiredUnresolvedExtensions = unresolvedExtensions.stream()
        .filter((e) -> {
          var source = this.resolvedDependencySources.get(e);
          return source == null || !source.isOptional();
        })
        .collect(Collectors.toList());

    if (!requiredUnresolvedExtensions.isEmpty()) {
      throw new ExtensionResolverException(this.manifest, requiredUnresolvedExtensions);
    }

    try {
      this.classLoader = new ExtensionClassLoader(this); // TODO: Custom URL scheme for extensions?
    } catch (MalformedURLException ex) {
      throw new ExtensionContainerException("Failed to open extension container", ex);
    }

    this.phase = Phase.LOADED;
  }

  /**
   * Performs the extension startup sequence (e.g. initializes the context, registers services,
   * etc).
   *
   * @param parentCtx a parent context from which the extension context will inherit.
   * @throws ExtensionContainerException when the startup fails due to a container related issue.
   */
  public void start(@NonNull ApplicationContext parentCtx) throws ExtensionContainerException {
    if (this.ctx != null) {
      return;
    }

    var failedDependencies = this.resolvedDependencies.stream()
        .filter((e) -> e.phase != Phase.RUNNING)
        .collect(Collectors.toList());

    if (!failedDependencies.isEmpty()) {
      throw new ExtensionResolverException(this.manifest, failedDependencies);
    }

    this.ctx = new AnnotationConfigApplicationContext();
    this.ctx.setParent(parentCtx);
    this.ctx.setClassLoader(this.classLoader);

    this.ctx.getBean(ConfigurableBeanFactory.class).registerSingleton("logger", this.logger);
    // TODO: Register service registration beans

    try {
      this.ctx.scan(this.manifest.getIdentifier());
      this.ctx.refresh();
      this.ctx.start();
    } catch (Throwable ex) {
      throw new ExtensionContainerException("Failed to start extension container", ex);
    }

    this.phase = Phase.RUNNING;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() {
    if (this.ctx != null) {
      try {
        this.ctx.close();
      } catch (Throwable ex) {
        this.logger.error("Failed to perform graceful shutdown", ex);
      }
      // TODO: Remove all registrations with the server (and other extensions)
      this.ctx = null;
    }

    if (this.classLoader != null) {
      try {
        this.classLoader.close();
      } catch (Throwable ex) {
        this.logger.error("Failed to close extension classloader", ex);
      }
      this.classLoader = null;
    }

    this.phase = Phase.REGISTERED;
    this.resolvedDependencies.clear();
  }
}
