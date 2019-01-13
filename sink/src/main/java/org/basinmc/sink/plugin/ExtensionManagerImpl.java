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
import org.basinmc.faucet.event.EventBus;
import org.basinmc.faucet.event.extension.ExtensionLoadEvent;
import org.basinmc.faucet.event.extension.ExtensionRegistrationEvent;
import org.basinmc.faucet.event.extension.ExtensionResolveEvent;
import org.basinmc.faucet.event.extension.ExtensionRunEvent;
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
  private final EventBus eventBus;
  private final Path pluginDir;

  // TODO: Given a copy on write list we won't need to sync as long as only one thread writes to this list
  private final Lock lock = new ReentrantLock();
  private final List<ExtensionImpl> extensions = new CopyOnWriteArrayList<>();
  private final List<Path> registrations = new CopyOnWriteArrayList<>(); // TODO: Probably won't need CopyOnWrite

  @Autowired
  public ExtensionManagerImpl(
      @NonNull ApplicationContext ctx,
      @NonNull EventBus eventBus,
      @NonNull @Value("${basin.extension.dir:extensions/}") Path pluginDir) {
    this.ctx = ctx;
    this.eventBus = eventBus;
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

      var state = this.eventBus.post(new ExtensionRegistrationEvent.Pre(extension));
      if (state.has(ExtensionRegistrationEvent.State.REGISTER)) {
        this.registrations.add(path);
        this.extensions.add(extension);
        this.eventBus.post(new ExtensionRegistrationEvent.Post(extension));
      }
    } catch (ExtensionException ex) {
      logger.error("Failed to load extension: " + path, ex);
    }
  }

  private void initialize() {
    logger.info("Extension system has entered startup");

    logger.debug("Performing dependency resolve on new extensions");
    this.extensions.stream()
        .filter((e) -> e.getPhase() == Phase.REGISTERED)
        .sorted()
        .forEach((e) -> {
          var state = this.eventBus.post(new ExtensionResolveEvent.Pre(e));
          if (!state.has(ExtensionResolveEvent.State.RESOLVE)) {
            return;
          }

          try {
            e.resolve();
            this.eventBus.post(new ExtensionResolveEvent.Post(e));
          } catch (Throwable ex) {
            logger.warn("Failed to resolve extension " + e.getManifest().getIdentifier() + "#" + e
                .getManifest().getVersion(), ex);
          }
        });

    logger.debug("Performing initialization on resolved extensions");
    this.extensions.stream()
        .filter((e) -> e.getPhase() == Phase.RESOLVED)
        .sorted()
        .forEach((e) -> {
          var state = this.eventBus.post(new ExtensionLoadEvent.Pre(e));
          if (!state.has(ExtensionLoadEvent.State.LOAD)) {
            return;
          }

          try {
            e.initialize();
            this.eventBus.post(new ExtensionLoadEvent.Post(e));
          } catch (Throwable ex) {
            logger
                .warn("Failed to initialize extension " + e.getManifest().getIdentifier() + "#" + e
                    .getManifest().getVersion(), ex);
            e.close(); // ensure loader is destroyed
          }
        });

    logger.debug("Performing startup on loaded extensions");
    this.extensions.stream()
        .filter((e) -> e.getPhase() == Phase.LOADED)
        .sorted()
        .forEach((e) -> {
          var state = this.eventBus.post(new ExtensionRunEvent.Pre(e));
          if (!state.has(ExtensionRunEvent.State.RUN)) {
            return;
          }

          try {
            e.start(this.ctx);
            this.eventBus.post(new ExtensionRunEvent.Post(e));
          } catch (Throwable ex) {
            logger
                .warn("Failed to start extension " + e.getManifest().getIdentifier() + "#" + e
                    .getManifest().getVersion(), ex);
            e.close(); // ensure context and loader are destroyed
          }
        });
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

  private void shutdown() {
    logger.info("Extension system is shutting down");

    logger.debug("Performing clean extension shutdown");
    this.extensions.stream()
        .filter((e) -> e.getPhase() == Phase.RUNNING)
        .sorted()
        .forEach(ExtensionImpl::close);
  }
}
