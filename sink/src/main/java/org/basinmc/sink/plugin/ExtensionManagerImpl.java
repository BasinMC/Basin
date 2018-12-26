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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.extension.Extension;
import org.basinmc.faucet.extension.Extension.Phase;
import org.basinmc.faucet.extension.ExtensionManager;
import org.basinmc.faucet.extension.error.ExtensionException;
import org.basinmc.sink.util.LifecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
@Service
public class ExtensionManagerImpl extends LifecycleService implements ExtensionManager {

  private static final Logger logger = LogManager.getFormatterLogger(ExtensionManagerImpl.class);

  private final ApplicationContext ctx;
  private final Path pluginDir;

  // TODO: Given a copy on write list we won't need to sync as long as only one thread writes to this list
  private final Lock lock = new ReentrantLock();
  private final List<ExtensionImpl> extensions = new CopyOnWriteArrayList<>();
  private final List<Path> registrations = new CopyOnWriteArrayList<>(); // TODO: Probably won't need CopyOnWrite

  @Autowired
  public ExtensionManagerImpl(
      @NonNull ApplicationContext ctx,
      @NonNull @Value("${basin.extension.dir:extensions/}") Path pluginDir) {
    this.ctx = ctx;
    this.pluginDir = pluginDir;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public List<Extension> getExtensions() {
    return Collections.unmodifiableList(this.extensions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onStart() {
    this.discover();
    this.initialize();

    super.onStart();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onStop() {
    this.shutdown();
    this.clearRegistry();

    super.onStop();
  }

  /**
   * Discovers all unregistered extension containers within the configured extension directory.
   */
  public void discover() {
    this.lock.lock();
    try {
      if (!Files.exists(this.pluginDir)) {
        try {
          Files.createDirectories(this.pluginDir);
          logger.info("Created an empty extension directory");
        } catch (IOException ex) {
          logger.warn("Cannot create extension directory", ex);
        }
        return;
      }

      Files.list(this.pluginDir)
          .filter((p) -> p.toString().endsWith(CONTAINER_EXTENSION))
          .forEach(this::discover);
    } catch (IOException ex) {
      logger.warn("Cannot index extension directory", ex);
    } finally {
      this.lock.unlock();
    }
  }

  public void discover(@NonNull Path path) {
    if (this.registrations.contains(path)) {
      return;
    }

    logger.debug("Indexing extension at path %s", path);
    try {
      var extension = new ExtensionImpl(path);

      this.registrations.add(path);
      this.extensions.add(extension);
    } catch (ExtensionException ex) {
      logger.error("Failed to load extension: " + path, ex);
    }
  }

  private void initialize() {
    logger.info("Extension system has entered startup");

    logger.debug("Performing dependency resolve on new extensions");
    this.extensions.stream()
        .filter((e) -> e.getPhase() == Phase.REGISTERED)
        .forEach((e) -> {
          // TODO: Wire dependencies
        });

    logger.debug("Performing initialization on resolved extensions");
    this.extensions.stream()
        .filter((e) -> e.getPhase() == Phase.RESOLVED)
        .forEach((e) -> {
          try {
            e.start(this.ctx);
          } catch (Throwable ex) {
            logger
                .warn("Failed to start extension " + e.getIdentifier() + "#" + e.getVersion(), ex);
            e.stop(); // ensure context is destroyed
          }
        });
  }

  private void shutdown() {
    logger.info("Extension system is shutting down");
  }

  private void clearRegistry() {
    this.lock.lock();
    try {
      this.extensions.clear();
      this.registrations.clear();
    } finally {
      this.lock.unlock();
    }
  }
}
