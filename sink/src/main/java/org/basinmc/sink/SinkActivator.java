/*
 * Copyright 2016 Johannes Donath <johannesd@torchmind.com>
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

import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.init.Bootstrap;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.datafix.DataFixesManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.FaucetVersion;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * Provides the base logic for initializing Sink within an OSGi compatible framework such as Apache
 * Felix or within Flange's embedded runtime.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class SinkActivator implements BundleActivator {

  private static final Logger logger = LogManager.getFormatterLogger(SinkActivator.class);
  private SinkServer server;

  /**
   * Replaces the PAX logging configuration against a more sane minecraft-like configuration.
   * TODO: This might be better located in the development bundle and/or flange.
   */
  private void configureLogger(@Nonnull BundleContext ctx) throws IOException {
    // extract configuration to an accessible location
    File configFile = ctx.getDataFile("log4j2.xml");

    try (InputStream inputStream = this.getClass().getResourceAsStream("/log4j2.xml")) {
      try (FileOutputStream outputStream = new FileOutputStream(configFile)) {
        ByteStreams.copy(inputStream, outputStream);
      }
    }

    // push config to pax logging
    ServiceReference<?> serviceReference = ctx
        .getServiceReference(ConfigurationAdmin.class.getName());

    if (serviceReference == null) {
      logger.warn("Could not replace logging configuration");
      logger.warn("You are about to experience a mindboggling amount of log messages!");
      return;
    }

    ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) ctx.getService(serviceReference);
    Configuration configuration = configurationAdmin
        .getConfiguration("org.ops4j.pax.logging", null);
    Dictionary<String, Object> properties = configuration.getProperties();

    if (properties == null) {
      properties = new Hashtable<>();
    }

    properties.put("org.ops4j.pax.logging.log4j2.config.file", configFile.toString());

    configuration.update(properties);
    logger.info("Loggers have been adjusted to sane levels.");
  }

  @Nonnull
  private DedicatedServer constructServerInstance(@Nonnull BundleContext ctx) {
    logger.info("Initializing Minecraft %s", FaucetVersion.API_VERSION);
    Bootstrap.register(); // apparently this is how the registries work ... don't question it

    // log some environment information
    logger.info("Running on Java v%s supplied by %s", System.getProperty("java.version", "Unknown"),
        System.getProperty("java.vendor"));

    // TODO: Integrate with plugins here?
    YggdrasilAuthenticationService var15 = new YggdrasilAuthenticationService(Proxy.NO_PROXY,
        UUID.randomUUID().toString());
    MinecraftSessionService var16 = var15.createMinecraftSessionService();
    GameProfileRepository var17 = var15.createProfileRepository();
    PlayerProfileCache var18 = new PlayerProfileCache(var17, new File(".", "usercache.json"));

    DedicatedServer server = new DedicatedServer(new File("."), DataFixesManager.createFixer(),
        var15, var16, var17, var18);

    // TODO: Re-introduce configuration

    if (!GraphicsEnvironment.isHeadless()) {
      logger.info("Server GUI has been disabled or is unavailable in this environment");
      // TODO: Custom GUI
    } else {
      logger
          .info("Server GUI has been disabled or is not available within the current environment");
    }

    return server;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start(@Nonnull BundleContext context) throws Exception {
    this.configureLogger(context);

    this.server = new SinkServer(context, this.constructServerInstance(context));
    this.server.start();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop(@Nonnull BundleContext context) throws Exception {
    this.server.shutdown(null);
  }
}