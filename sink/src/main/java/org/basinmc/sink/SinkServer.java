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

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.Handled;
import org.basinmc.faucet.Server;
import org.springframework.stereotype.Service;

/**
 * Abstracts access to the server configuration and state.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
@Service
public class SinkServer implements Server, Handled<DedicatedServer> {

  private static final Logger logger = LogManager.getFormatterLogger(SinkServer.class);

  private final DedicatedServer server;
  private final Configuration configuration = new Configuration();

  public SinkServer(@NonNull DedicatedServer server) {
    this.server = server;
    logger.debug("Faucet services are ready for consumption");
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public String getVersion() {
    return this.server.getMinecraftVersion();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOnlineMode() {
    return this.server.isServerInOnlineMode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shutdown(@Nullable String reason) {
    // TODO Fire shutdown event here, once I add it. :)
    this.server.logInfo("Server Shutdown: " + reason);

    DedicatedPlayerList playerList = this.server.getPlayerList();
    playerList.getPlayers().forEach(EntityPlayerMP::disconnect); // TODO: Restore reason

    this.server.stopServer();
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Path getBaseDirectory() {
    // TODO: Add support for a variable base directory since this might be useful in order to share jars
    return Paths.get(".");
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Server.Configuration getConfiguration() {
    return this.configuration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLifeTime() {
    return this.server.getTickCounter();
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public DedicatedServer getHandle() {
    return this.server;
  }

  private class Configuration implements Server.Configuration {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areCommandBlocksEnabled() {
      return SinkServer.this.server.isCommandBlockEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public String getHostname() {
      return SinkServer.this.server.getServerHostname();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumConcurrentPlayers() {
      return SinkServer.this.server.getMaxPlayers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumBuildHeight() {
      return SinkServer.this.server.getBuildLimit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getMaximumTickTime() {
      return SinkServer.this.server.getMaxTickTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNetworkCompressionThreshold() {
      return SinkServer.this.server.getNetworkCompressionThreshold();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOperatorPermissionLevel() {
      return SinkServer.this.server.getOpPermissionLevel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerIdleTimeout() {
      return SinkServer.this.server.getMaxPlayerIdleMinutes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getQueryPort() {
//      return SinkServer.this.server.settings.getIntProperty("query.port", 25565);
      return 0; // FIXME
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public String getRemoteConsolePassword() {
//      return SinkServer.this.server.settings.getStringProperty("rcon.password", "");
      return ""; // FIXME
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRemoteConsolePort() {
//      return SinkServer.this.server.settings.getIntProperty("rcon.port", 25575);
      return 0; // FIXME
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSpawnProtectionRadius() {
      return SinkServer.this.server.getSpawnProtectionSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getViewDistance() {
//      return SinkServer.this.server.settings.getIntProperty("view-distance", 10);
      return 0; // FIXME
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFlightAllowed() {
      return SinkServer.this.server.isFlightAllowed();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isGamemodeForced() {
      return SinkServer.this.server.getForceGamemode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHardcore() {
      return SinkServer.this.server.isHardcore();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNativeTransportEnabled() {
      return SinkServer.this.server.shouldUseNativeTransport();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNetherAllowed() {
      return SinkServer.this.server.getAllowNether();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPvpEnabled() {
      return SinkServer.this.server.isPVPEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRemoteConsoleEnabled() {
//      return SinkServer.this.server.settings.getBooleanProperty("enable-rcon", false);
      return false; // FIXME
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isQueryEnabled() {
//      return SinkServer.this.server.settings.getBooleanProperty("enable-query", false);
      return false; // FIXME
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSnooperEnabled() {
      return SinkServer.this.server.isSnooperEnabled();
    }
  }
}
