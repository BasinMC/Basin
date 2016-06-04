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
package org.basinmc.sink;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

import org.basinmc.faucet.Server;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nonnull;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class SinkServer implements Server {
    private final DedicatedServer server;
    private final Server.Configuration configuration = new Configuration();

    public SinkServer(DedicatedServer server) {
        this.server = server;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
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
    @Nonnull
    @Override
    public Path getBaseDirectory() {
        return Paths.get(".");
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
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

    private class Configuration implements Server.Configuration {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean areAchievementAnnouncementsEnabled() {
            return SinkServer.this.server.isAnnouncingPlayerAchievements();
        }

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
        @Nonnull
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
            return SinkServer.this.server.getSettings().getIntProperty("player-idle-timeout", 0);
        }

        @Override
        public int getQueryPort() {
            return SinkServer.this.server.getSettings().getIntProperty("query.port", 25565);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public String getRemoteConsolePassword() {
            return SinkServer.this.server.getSettings().getStringProperty("rcon.password", "");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getRemoteConsolePort() {
            return SinkServer.this.server.getSettings().getIntProperty("rcon.port", 25575);
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
            return SinkServer.this.server.getSettings().getIntProperty("view-distance", 10);
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
            return SinkServer.this.server.getSettings().getBooleanProperty("enable-rcon", false);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isQueryEnabled() {
            return SinkServer.this.server.getSettings().getBooleanProperty("enable-query", false);
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
