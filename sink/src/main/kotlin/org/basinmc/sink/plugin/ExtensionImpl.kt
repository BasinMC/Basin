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
import org.apache.logging.log4j.Logger
import org.basinmc.faucet.extension.Extension
import org.basinmc.faucet.extension.dependency.ExtensionDependency
import org.basinmc.faucet.extension.dependency.ServiceDependency
import org.basinmc.faucet.extension.error.ExtensionAccessException
import org.basinmc.faucet.extension.error.ExtensionContainerException
import org.basinmc.faucet.extension.error.ExtensionException
import org.basinmc.faucet.extension.error.ExtensionResolverException
import org.basinmc.sink.plugin.manifest.ExtensionManifestImpl
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.io.IOException
import java.net.MalformedURLException
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
class ExtensionImpl @Throws(ExtensionException::class) internal constructor(
    /**
     * Retrieves the path to the container file from which this extension definition originates.
     *
     * @return a container file path.
     */
    val containerPath: Path) : AutoCloseable, Extension {

  override val manifest: ExtensionManifestImpl = try {
    FileChannel.open(containerPath, StandardOpenOption.READ)
        .use(::ExtensionManifestImpl)
  } catch (ex: IOException) {
    throw ExtensionAccessException("Cannot read container file", ex)
  }

  private var _phase: Extension.Phase = Extension.Phase.REGISTERED
  override val phase: Extension.Phase
    get() = this._phase

  private val logger: Logger = LogManager.getFormatterLogger(this.manifest.displayName)

  private val resolvedDependencySources = HashMap<ExtensionImpl, ExtensionDependency>()
  private val _resolvedDependencies = mutableListOf<ExtensionImpl>()
  val resolvedDependencies: List<ExtensionImpl>
    get() = this._resolvedDependencies

  private var _classLoader: ExtensionClassLoader? = null
  val classLoader: ExtensionClassLoader?
    get() = this._classLoader

  private var _context: AnnotationConfigApplicationContext? = null
  override val context: AnnotationConfigApplicationContext?
    get() = this._context

  /**
   * Wires a dependency into this extension.
   *
   * @param extension an extension.
   * @param dependency the dependency which is fulfilled by this wiring (if any).
   * @throws IllegalStateException when invoked outside of the resolve (e.g. [Phase.LOADED]
   * phase).
   */
  fun wireDependency(extension: ExtensionImpl,
      dependency: ExtensionDependency?) {
    if (this.phase != Extension.Phase.REGISTERED) {
      throw IllegalStateException("Cannot wire dependency in " + this.phase + " phase")
    }

    this._resolvedDependencies += extension

    if (dependency != null) {
      this.resolvedDependencySources[extension] = dependency
    }
  }

  @Throws(ExtensionContainerException::class)
  internal fun resolve() {
    val unresolvedExtensions = this.manifest.extensionDependencies
        .filter { dep -> !dep.isOptional }
        .filter { dep -> this.resolvedDependencies.none { dep.matches(it.manifest) } }

    // TODO: Resolve services
    val unresolvedServices = emptyList<ServiceDependency>()

    if (!unresolvedExtensions.isEmpty() || !unresolvedServices.isEmpty()) {
      throw ExtensionResolverException(this.manifest, unresolvedExtensions,
          unresolvedServices)
    }

    this._phase = Extension.Phase.RESOLVED
  }

  /**
   * Initializes the extension class loader along with all of its dependencies.
   *
   * @throws ExtensionContainerException when the container cannot be accessed.
   */
  @Throws(ExtensionContainerException::class)
  internal fun initialize() {
    if (this.context != null) {
      return
    }

    val unresolvedExtensions = this.resolvedDependencies
        .filter { e -> e.phase != Extension.Phase.LOADED }

    unresolvedExtensions.stream()
        .filter { e ->
          val source = this.resolvedDependencySources[e]
          source != null && source.isOptional
        }
        .forEach { e ->
          this.logger
              .debug("Removed unresolved optional dependency to %s v%s", e.manifest.identifier,
                  e.manifest.version)

          this._resolvedDependencies -= e
          this.resolvedDependencySources -= e
        }
    val requiredUnresolvedExtensions = unresolvedExtensions
        .filter { e ->
          this.resolvedDependencySources[e]?.takeIf(ExtensionDependency::isOptional) == null
        }

    if (!requiredUnresolvedExtensions.isEmpty()) {
      throw ExtensionResolverException(this.manifest, requiredUnresolvedExtensions)
    }

    // TODO: Check for package overlaps

    try {
      this._classLoader = ExtensionClassLoader(this) // TODO: Custom URL scheme for extensions?
    } catch (ex: MalformedURLException) {
      throw ExtensionContainerException("Failed to open extension container", ex)
    }

    this._phase = Extension.Phase.LOADED
  }

  /**
   * Performs the extension startup sequence (e.g. initializes the context, registers services,
   * etc).
   *
   * @param parentContext a parent context from which the extension context will inherit.
   * @throws ExtensionContainerException when the startup fails due to a container related issue.
   */
  @Throws(ExtensionContainerException::class)
  internal fun start(parentContext: ApplicationContext) {
    if (this.context != null) {
      return
    }

    val failedDependencies = this.resolvedDependencies
        .filter { e -> e.phase != Extension.Phase.RUNNING }

    if (!failedDependencies.isEmpty()) {
      throw ExtensionResolverException(this.manifest, failedDependencies)
    }

    this._context = AnnotationConfigApplicationContext().let { ctx ->
      ctx.parent = parentContext
      ctx.classLoader = this.classLoader

      ctx.getBean(ConfigurableBeanFactory::class.java)
          .registerSingleton("logger", this.logger)

      try {
        ctx.scan(this.manifest.identifier)
        ctx.refresh()
        ctx.start()
      } catch (ex: Throwable) {
        throw ExtensionContainerException("Failed to start extension container", ex)
      }

      this._phase = Extension.Phase.RUNNING

      ctx
    }
  }

  /**
   * {@inheritDoc}
   */
  override fun close() {
    if (this.context != null) {
      try {
        this.context!!.close()
      } catch (ex: Throwable) {
        this.logger.error("Failed to perform graceful shutdown", ex)
      }

      // TODO: Remove all registrations with the server (and other extensions)
      this._context = null
    }

    if (this.classLoader != null) {
      try {
        this.classLoader!!.close()
      } catch (ex: Throwable) {
        this.logger.error("Failed to close extension classloader", ex)
      }

      this._classLoader = null
    }

    this._phase = Extension.Phase.REGISTERED
    this._resolvedDependencies.clear()
  }
}
