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
package org.basinmc.sink;

import edu.umd.cs.findbugs.annotations.NonNull;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Provides utility methods which permit the initialization of the Basin application and extension
 * context during the server startup.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class Sink implements AutoCloseable {

  private static final Logger logger = LogManager.getFormatterLogger(Sink.class);

  private final AnnotationConfigApplicationContext context;

  public Sink(@NonNull MinecraftServer server) {
    this.context = new AnnotationConfigApplicationContext();
    this.context.getBeanFactory().registerSingleton("minecraftServer", server);

    this.context.scan(this.getClass().getPackageName());
  }

  public void onStart() {
    logger.info("Basin Sink v%s entered startup", SinkVersion.INSTANCE.getVersion());

    logger.debug("Performing Spring Context initialization");
    this.context.refresh();
    this.context.start();

    // TODO: Initialize extension system
    // TODO: Publish startup event
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() {
    logger.info("Sink is shutting down");

    // TODO: Publish close event
    this.context.close();
  }
}
