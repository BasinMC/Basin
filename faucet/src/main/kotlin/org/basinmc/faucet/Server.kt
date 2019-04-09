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
package org.basinmc.faucet

import java.nio.file.Path

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
interface Server {

  /**
   * Retrieves a path pointing at the base server directory.
   */
  val baseDirectory: Path

  /**
   * Retrieves a mutable representation of the server configuration.
   */
  val configuration: Configuration

  /**
   * Retrieves the overall time the server has been running for in game ticks.
   *
   * This number is equal to the amount of ticks processed by the server during its runtime and thus
   * may differ from the usual 20 ticks = 1 second scale.
   */
  val lifeTime: Int

  /**
   * Retrieves the currently active server version (as in game version such as 1.9.4 or 1.10).
   */
  val version: String

  /**
   * Checks whether the server authenticates against Mojang or whether any player can join
   * regardless of authentication.
   *
   * @return true if in online mode, false otherwise.
   */
  val isOnlineMode: Boolean

  /**
   * Stop the server gracefully with a given reason. The reason will be printed to the console and
   * optionally broadcast to players (as part of the kick message). Why is this method deprecated
   * from the beginning, you ask? Because it should theoretically use a chat component API that I
   * haven't designed yet. TODO.
   *
   * @param reason The reason the server is shutting down (if null, "Server Shutdown" is used).
   */
  @Deprecated("")
  fun shutdown(reason: String?)

  interface Configuration {

    /**
     * Retrieves the address the server is configured to listen on.
     */
    val hostname: String

    /**
     * Retrieves the maximum amount of concurrent players connected to the server.
     */
    val maximumConcurrentPlayers: Int

    /**
     * Retrieves the maximum height players are allowed to build at.
     */
    val maximumBuildHeight: Int

    /**
     * Retrieves the maximum amount a server is allowed to wait for a single tick to process before
     * assuming the server is hanging.
     *
     * A value of -1 indicates, that the watchdog shall not shutdown the server regardless of how
     * long a tick needs to process.
     */
    val maximumTickTime: Long

    /**
     * Retrieves the threshold at which plugins will be compressed.
     *
     * @return a threshold in bytes.
     */
    val networkCompressionThreshold: Int

    /**
     * Retrieves the operator permission level.
     *
     * Each level grants an additional set of permissions in the vanilla permission system as
     * follows:   * **1** - Allows operators to bypass spawn protection.  *
     * **2** - Allows operators to utilize /clear, /difficulty, /effect, /gamemode,
     * /gamerule, /give, and /tp, and can edit command blocks.   * **3** - Allows
     * operators to utilize /ban, /deop, /kick, and /op  * **4** - Allows
     * operators to utilize /stop
     *
     * @return an operator permission level.
     */
    val operatorPermissionLevel: Int

    /**
     * Retrieves the maximum amount of minutes a player is allowed to idle before being kicked from
     * the server.
     *
     * If set to zero, players will not be kicked for idling.
     */
    val playerIdleTimeout: Int

    /**
     * Retrieves the port the server is listening on for server status queries.
     */
    val queryPort: Int

    /**
     * Retrieves the password used for authentication purposes on the remote console (RCon) server.
     */
    val remoteConsolePassword: String

    /**
     * Retrieves the port the server is listening on for remote console (RCon) connections.
     */
    val remoteConsolePort: Int

    /**
     * Retrieves the radius (in blocks) which is protected from building by operators.
     */
    val spawnProtectionRadius: Int

    /**
     * Retrieves a player's view distance in chunks.
     */
    val viewDistance: Int

    /**
     * Checks whether flying is allowed outside of creative mode.
     *
     * @return true if allowed, false otherwise.
     */
    val isFlightAllowed: Boolean

    /**
     * Checks whether player game modes will be updated according to the default setting upon
     * logging into the server.
     *
     * @return true if enabled, false otherwise.
     */
    val isGamemodeForced: Boolean

    /**
     * Checks whether a world is set to hardcore and thus does not allow players to respawn.
     *
     * @return true if hardcore, false otherwise.
     */
    val isHardcore: Boolean

    /**
     * Checks whether native transport is enabled on the server.
     *
     * Note: Even if enabled, this feature is only supported on Linux machines as it requires
     * netty's native epoll implementation to be present.
     *
     * @return true if enabled, false otherwise.
     */
    val isNativeTransportEnabled: Boolean

    /**
     * Checks whether players are allowed to enter the nether.
     *
     * @return true if allowed, false otherwise.
     */
    val isNetherAllowed: Boolean

    /**
     * Checks whether PvP (Player vs. Player) is allowed on the server.
     *
     * @return true if enabled, false otherwise.
     */
    val isPvpEnabled: Boolean

    /**
     * Checks whether the server listens for remote console (RCon) connections.
     *
     * @return true if enabled, false otherwise.
     */
    val isRemoteConsoleEnabled: Boolean

    /**
     * Checks whether the query server is enabled.
     *
     * @return true if enabled, false otherwise.
     */
    val isQueryEnabled: Boolean

    /**
     * Checks whether Minecraft's statistics collection is enabled.
     *
     * @return true if enabled, false otherwise.
     */
    val isSnooperEnabled: Boolean

    /**
     * Checks whether command blocks are enabled on the server.
     *
     * When true, opped players in creative mode will be able to alter command blocks as well as
     * place new ones in the world.
     *
     * @return true if enabled, false otherwise.
     */
    val commandBlocksEnabled: Boolean
  }
}
