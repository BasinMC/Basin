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

import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.LogManager
import org.basinmc.faucet.Handled
import org.basinmc.faucet.Server
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Abstracts access to the server configuration and state.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
@Service
class SinkServer(
    /**
     * {@inheritDoc}
     */
    override val handle: MinecraftServer) : Server, Handled<MinecraftServer> {

  override val configuration: Server.Configuration = Configuration()

  /**
   * {@inheritDoc}
   */
  override val version: String
    get() = this.handle.minecraftVersion

  /**
   * {@inheritDoc}
   */
  override val isOnlineMode: Boolean
    get() = this.handle.isServerInOnlineMode

  /**
   * {@inheritDoc}
   */
  override// TODO: Add support for a variable base directory since this might be useful in order to share jars
  val baseDirectory: Path
    get() = Paths.get(".")

  /**
   * {@inheritDoc}
   */
  override val lifeTime: Int
    get() = this.handle.tickCounter

  init {
    logger.debug("Faucet services are ready for consumption")
  }

  /**
   * {@inheritDoc}
   */
  override fun shutdown(reason: String?) {
    // TODO Fire shutdown event here, once I add it. :)
    this.handle.logInfo("Server Shutdown: " + reason!!)

    val playerList = this.handle.playerList
    playerList.players.forEach(EntityPlayerMP::disconnect) // TODO: Restore reason

    this.handle.stopServer()
  }

  private inner class Configuration : Server.Configuration {

    /**
     * {@inheritDoc}
     */
    override val commandBlocksEnabled: Boolean
      get() = this@SinkServer.handle.isCommandBlockEnabled

    /**
     * {@inheritDoc}
     */
    override val hostname: String
      get() = this@SinkServer.handle.serverHostname

    /**
     * {@inheritDoc}
     */
    override val maximumConcurrentPlayers: Int
      get() = this@SinkServer.handle.maxPlayers

    /**
     * {@inheritDoc}
     */
    override val maximumBuildHeight: Int
      get() = this@SinkServer.handle.buildLimit

    /**
     * {@inheritDoc}
     */
    override val maximumTickTime: Long
      get() = 0 // SinkServer.this.server.getMaxTickTime();

    /**
     * {@inheritDoc}
     */
    override val networkCompressionThreshold: Int
      get() = this@SinkServer.handle.networkCompressionThreshold

    /**
     * {@inheritDoc}
     */
    override val operatorPermissionLevel: Int
      get() = this@SinkServer.handle.opPermissionLevel

    /**
     * {@inheritDoc}
     */
    override val playerIdleTimeout: Int
      get() = this@SinkServer.handle.maxPlayerIdleMinutes

    /**
     * {@inheritDoc}
     */
    // FIXME
    override val queryPort: Int
      get() = 0 // SinkServer.this.server.settings.getIntProperty("query.port", 25565);

    /**
     * {@inheritDoc}
     */
    // FIXME
    override val remoteConsolePassword: String
      get() = "" // SinkServer.this.server.settings.getStringProperty("rcon.password", "");

    /**
     * {@inheritDoc}
     */
    // FIXME
    override val remoteConsolePort: Int
      get() = 0 // SinkServer.this.server.settings.getIntProperty("rcon.port", 25575);

    /**
     * {@inheritDoc}
     */
    override val spawnProtectionRadius: Int
      get() = this@SinkServer.handle.spawnProtectionSize

    /**
     * {@inheritDoc}
     */
    // FIXME
    override val viewDistance: Int
      get() = 0 // SinkServer.this.server.settings.getIntProperty("view-distance", 10);

    /**
     * {@inheritDoc}
     */
    override val isFlightAllowed: Boolean
      get() = this@SinkServer.handle.isFlightAllowed

    /**
     * {@inheritDoc}
     */
    override val isGamemodeForced: Boolean
      get() = this@SinkServer.handle.forceGamemode

    /**
     * {@inheritDoc}
     */
    override val isHardcore: Boolean
      get() = this@SinkServer.handle.isHardcore

    /**
     * {@inheritDoc}
     */
    override val isNativeTransportEnabled: Boolean
      get() = this@SinkServer.handle.shouldUseNativeTransport()

    /**
     * {@inheritDoc}
     */
    override val isNetherAllowed: Boolean
      get() = this@SinkServer.handle.allowNether

    /**
     * {@inheritDoc}
     */
    override val isPvpEnabled: Boolean
      get() = this@SinkServer.handle.isPVPEnabled

    /**
     * {@inheritDoc}
     */
    // FIXME
    override val isRemoteConsoleEnabled: Boolean
      get() = false // SinkServer.this.server.settings.getBooleanProperty("enable-rcon", false);

    /**
     * {@inheritDoc}
     */
    // FIXME
    override val isQueryEnabled: Boolean
      get() = false // SinkServer.this.server.settings.getBooleanProperty("enable-query", false);

    /**
     * {@inheritDoc}
     */
    override val isSnooperEnabled: Boolean
      get() = this@SinkServer.handle.isSnooperEnabled
  }

  companion object {

    private val logger = LogManager.getFormatterLogger(SinkServer::class.java)
  }
}
