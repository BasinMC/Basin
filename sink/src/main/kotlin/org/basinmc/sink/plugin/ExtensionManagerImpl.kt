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
package org.basinmc.sink.plugin

import org.apache.logging.log4j.LogManager
import org.basinmc.faucet.event.EventBus
import org.basinmc.faucet.event.extension.*
import org.basinmc.faucet.extension.Extension.Phase
import org.basinmc.faucet.extension.ExtensionManager
import org.basinmc.faucet.extension.error.ExtensionException
import org.basinmc.sink.util.LifecycleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.locks.ReentrantLock

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
@Service
class ExtensionManagerImpl @Autowired
constructor(
    private val ctx: ApplicationContext,
    private val eventBus: EventBus,
    @param:Value("\${basin.extension.dir:extensions/}") private val pluginDir: Path) :
    LifecycleService(), ExtensionManager {

  private val _extensions = CopyOnWriteArrayList<ExtensionImpl>()
  override val extensions: List<ExtensionImpl>
    get() = this._extensions

  // TODO: Given a copy on write list we won't need to sync as long as only one thread writes to this list
  private val lock = ReentrantLock()
  private val registrations = CopyOnWriteArrayList<Path>() // TODO: Probably won't need CopyOnWrite

  /**
   * {@inheritDoc}
   */
  override fun onStart() {
    this.discover()
    this.initialize()

    super.onStart()
  }

  /**
   * {@inheritDoc}
   */
  override fun onStop() {
    this.shutdown()
    this.clearRegistry()

    super.onStop()
  }

  /**
   * Discovers all unregistered extension containers within the configured extension directory.
   */
  fun discover() {
    this.lock.lock()
    try {
      if (!Files.exists(this.pluginDir)) {
        try {
          Files.createDirectories(this.pluginDir)
          logger.info("Created an empty extension directory")
        } catch (ex: IOException) {
          logger.warn("Cannot create extension directory", ex)
        }

        return
      }

      Files.list(this.pluginDir)
          .filter { p -> p.toString().endsWith(ExtensionManager.CONTAINER_EXTENSION) }
          .forEach(this::discover)
    } catch (ex: IOException) {
      logger.warn("Cannot index extension directory", ex)
    } finally {
      this.lock.unlock()
    }
  }

  fun discover(path: Path) {
    if (this.registrations.contains(path)) {
      return
    }

    logger.debug("Indexing extension at path %s", path)
    try {
      val extension = ExtensionImpl(path)

      val state = this.eventBus.post(ExtensionRegistrationEvent.Pre(extension))
      if (state.has(ExtensionRegistrationEvent.State.REGISTER)) {
        this.registrations.add(path)
        this._extensions += extension
        this.eventBus.post(ExtensionRegistrationEvent.Post(extension))
      }
    } catch (ex: ExtensionException) {
      logger.error("Failed to load extension: $path", ex)
    }

  }

  private fun initialize() {
    logger.info("Extension system has entered startup")

    logger.debug("Performing dependency resolve on new extensions")
    this.extensions
        .filter { e -> e.phase == Phase.REGISTERED }
        .sorted()
        .forEach { e ->
          val state = this.eventBus.post(ExtensionResolveEvent.Pre(e))
          if (!state.has(ExtensionResolveEvent.State.RESOLVE)) {
            return@forEach
          }

          try {
            e.resolve()
            this.eventBus.post(ExtensionResolveEvent.Post(e))
          } catch (ex: Throwable) {
            logger.warn("Failed to resolve extension " + e.manifest.identifier + "#" + e
                .manifest.version, ex)
          }
        }

    logger.debug("Performing initialization on resolved extensions")
    this.extensions
        .filter { e -> e.phase == Phase.RESOLVED }
        .sorted()
        .forEach { e ->
          val state = this.eventBus.post(ExtensionLoadEvent.Pre(e))
          if (!state.has(ExtensionLoadEvent.State.LOAD)) {
            return@forEach
          }

          try {
            e.initialize()
            this.eventBus.post(ExtensionLoadEvent.Post(e))
          } catch (ex: Throwable) {
            logger
                .warn("Failed to initialize extension " + e.manifest.identifier + "#" + e
                    .manifest.version, ex)
            e.close() // ensure loader is destroyed
          }
        }

    logger.debug("Performing startup on loaded extensions")
    this.extensions
        .filter { e -> e.phase == Phase.LOADED }
        .sorted()
        .forEach { e ->
          val state = this.eventBus.post(ExtensionRunEvent.Pre(e))
          if (!state.has(ExtensionRunEvent.State.RUN)) {
            return@forEach
          }

          try {
            e.start(this.ctx)
            this.eventBus.post(ExtensionRunEvent.Post(e))
          } catch (ex: Throwable) {
            logger
                .warn("Failed to start extension " + e.manifest.identifier + "#" + e
                    .manifest.version, ex)
            e.close() // ensure context and loader are destroyed
          }
        }

    logger.info("Extension system startup complete")
  }

  private fun clearRegistry() {
    this.lock.lock()
    try {
      val extensions = ArrayList(this.extensions)
      extensions.forEach { e -> this.eventBus.post(ExtensionRemovalEvent.Pre(e)) }

      this._extensions.clear()
      this.registrations.clear()

      extensions.forEach { e -> this.eventBus.post(ExtensionRemovalEvent.Post(e)) }
    } finally {
      this.lock.unlock()
    }
  }

  private fun shutdown() {
    logger.info("Extension system is shutting down")

    logger.debug("Performing clean extension shutdown")
    this.extensions
        .filter { e -> e.phase == Phase.RUNNING }
        .sorted()
        .forEach { e ->
          this.eventBus.post(ExtensionShutdownEvent.Pre(e))

          try {
            e.close()
          } catch (ex: Throwable) {
            logger.warn("Failed to perform graceful shutdown of extension " + e.manifest
                .identifier + "#" + e.manifest.version, ex)
          }

          this.eventBus.post(ExtensionShutdownEvent.Post(e))
        }
  }

  companion object {

    private val logger = LogManager.getFormatterLogger(ExtensionManagerImpl::class.java)
  }
}
