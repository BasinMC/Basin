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
package org.basinmc.sink

import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.LogManager
import org.springframework.context.annotation.AnnotationConfigApplicationContext

/**
 * Provides utility methods which permit the initialization of the Basin application and extension
 * context during the server startup.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
class Sink(server: MinecraftServer) : AutoCloseable {

  private val context: AnnotationConfigApplicationContext = AnnotationConfigApplicationContext().let {
    it.beanFactory.registerSingleton("minecraftServer", server)
    it.scan(this.javaClass.packageName)

    it
  }

  fun onStart() {
    logger.info("Basin Sink v%s entered startup", SinkVersion.version)

    logger.debug("Performing Spring Context initialization")
    this.context.refresh()
    this.context.start()

    // TODO: Initialize extension system
    // TODO: Publish startup event
  }

  /**
   * {@inheritDoc}
   */
  override fun close() {
    logger.info("Sink is shutting down")

    // TODO: Publish close event
    this.context.close()
  }

  companion object {

    private val logger = LogManager.getFormatterLogger(Sink::class.java)
  }
}
