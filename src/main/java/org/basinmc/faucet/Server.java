/*
 * Copyright 2016 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.basinmc.faucet;

import java.nio.file.Path;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.Signed;
import org.osgi.framework.Bundle;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface Server {

  /**
   * Retrieves the currently active API version.
   */
  @Nonnull
  default String getApiVersion() {
    Package p = this.getClass().getPackage();

    if (p != null) {
      String specificationVersion = p.getSpecificationVersion();

      if (specificationVersion != null) {
        return specificationVersion;
      }
    }

    return "1.0.0-SNAPSHOT";
  }

  /**
   * Retrieves a path pointing at the base server directory.
   */
  @Nonnull
  Path getBaseDirectory();

  /**
   * Retrieves a mutable representation of the server configuration.
   */
  @Nonnull
  Configuration getConfiguration();

  /**
   * Retrieves the bundle which is providing the implementation for this and other Faucet
   * implementations.
   */
  @Nonnull
  Bundle getImplementationBundle();

  /**
   * Retrieves the overall time the server has been running for in game ticks.
   *
   * This number is equal to the amount of ticks processed by the server during its runtime and
   * thus may differ from the usual 20 ticks = 1 second scale.
   */
  @Nonnegative
  int getLifeTime();

  /**
   * Retrieves the currently active server version (as in game version such as 1.9.4 or 1.10).
   */
  @Nonnull
  String getVersion();

  /**
   * Checks whether the server authenticates against Mojang or whether any player can join
   * regardless of authentication.
   *
   * @return true if in online mode, false otherwise.
   */
  boolean isOnlineMode();

  /**
   * Stop the server gracefully with a given reason. The reason will be printed to the console and
   * optionally broadcast to players (as part of the kick message). Why is this method deprecated
   * from the beginning, you ask? Because it should theoretically use a chat component API that I
   * haven't designed yet. TODO.
   *
   * @param reason The reason the server is shutting down (if null, "Server Shutdown" is used).
   */
  @Deprecated
  void shutdown(@Nullable String reason);

  interface Configuration {

    /**
     * Checks whether achievements will be publicly announced in chat.
     *
     * @return true if enabled, false otherwise.
     */
    boolean areAchievementAnnouncementsEnabled();

    /**
     * Checks whether command blocks are enabled on the server.
     *
     * When true, opped players in creative mode will be able to alter command blocks as well as
     * place new ones in the world.
     *
     * @return true if enabled, false otherwise.
     */
    boolean areCommandBlocksEnabled();

    /**
     * Retrieves the address the server is configured to listen on.
     */
    @Nonnull
    String getHostname();

    /**
     * Retrieves the maximum amount of concurrent players connected to the server.
     */
    @Nonnegative
    int getMaximumConcurrentPlayers();

    /**
     * Retrieves the maximum height players are allowed to build at.
     */
    @Nonnegative
    int getMaximumBuildHeight();

    /**
     * Retrieves the maximum amount a server is allowed to wait for a single tick to process
     * before assuming the server is hanging.
     *
     * A value of -1 indicates, that the watchdog shall not shutdown the server regardless of
     * how long a tick needs to process.
     */
    @Signed
    long getMaximumTickTime();

    /**
     * Retrieves the threshold at which plugins will be compressed.
     *
     * @return a threshold in bytes.
     */
    @Signed
    int getNetworkCompressionThreshold();

    /**
     * Retrieves the operator permission level.
     *
     * Each level grants an additional set of permissions in the vanilla permission system as
     * follows: <ul> <li><strong>1</strong> - Allows operators to bypass spawn protection.</li>
     * <li> <strong>2</strong> - Allows operators to utilize /clear, /difficulty, /effect,
     * /gamemode, /gamerule, /give, and /tp, and can edit command blocks. </li>
     * <li><strong>3</strong> - Allows operators to utilize /ban, /deop, /kick, and /op</li>
     * <li><strong>4</strong> - Allows operators to utilize /stop</li> </ul>
     *
     * @return an operator permission level.
     */
    @Nonnegative
    int getOperatorPermissionLevel();

    /**
     * Retrieves the maximum amount of minutes a player is allowed to idle before being kicked
     * from the server.
     *
     * If set to zero, players will not be kicked for idling.
     */
    int getPlayerIdleTimeout();

    /**
     * Retrieves the port the server is listening on for server status queries.
     */
    @Nonnegative
    int getQueryPort();

    /**
     * Retrieves the password used for authentication purposes on the remote console (RCon)
     * server.
     */
    @Nonnull
    String getRemoteConsolePassword();

    /**
     * Retrieves the port the server is listening on for remote console (RCon) connections.
     */
    @Nonnegative
    int getRemoteConsolePort();

    /**
     * Retrieves the radius (in blocks) which is protected from building by operators.
     */
    @Nonnegative
    int getSpawnProtectionRadius();

    /**
     * Retrieves a player's view distance in chunks.
     */
    @Nonnegative
    int getViewDistance();

    /**
     * Checks whether flying is allowed outside of creative mode.
     *
     * @return true if allowed, false otherwise.
     */
    boolean isFlightAllowed();

    /**
     * Checks whether player game modes will be updated according to the default setting upon
     * logging into the server.
     *
     * @return true if enabled, false otherwise.
     */
    boolean isGamemodeForced();

    /**
     * Checks whether a world is set to hardcore and thus does not allow players to respawn.
     *
     * @return true if hardcore, false otherwise.
     */
    boolean isHardcore();

    /**
     * Checks whether native transport is enabled on the server.
     *
     * Note: Even if enabled, this feature is only supported on Linux machines as it requires
     * netty's native epoll implementation to be present.
     *
     * @return true if enabled, false otherwise.
     */
    boolean isNativeTransportEnabled();

    /**
     * Checks whether players are allowed to enter the nether.
     *
     * @return true if allowed, false otherwise.
     */
    boolean isNetherAllowed();

    /**
     * Checks whether PvP (Player vs. Player) is allowed on the server.
     *
     * @return true if enabled, false otherwise.
     */
    boolean isPvpEnabled();

    /**
     * Checks whether the server listens for remote console (RCon) connections.
     *
     * @return true if enabled, false otherwise.
     */
    boolean isRemoteConsoleEnabled();

    /**
     * Checks whether the query server is enabled.
     *
     * @return true if enabled, false otherwise.
     */
    boolean isQueryEnabled();

    /**
     * Checks whether Minecraft's statistics collection is enabled.
     *
     * @return true if enabled, false otherwise.
     */
    boolean isSnooperEnabled();
  }
}
