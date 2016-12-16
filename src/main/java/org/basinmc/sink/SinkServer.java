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

import net.minecraft.server.dedicated.DedicatedServer;

import org.basinmc.faucet.Handled;
import org.basinmc.faucet.Server;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class SinkServer implements Server, Handled<DedicatedServer> {
    private final DedicatedServer server;
    private final Server.Configuration configuration = new Configuration();

    public SinkServer(DedicatedServer server) {
        this.server = server;

        Runtime.getRuntime().addShutdownHook(new Thread("Sink Shutdown Thread") {
            @Override
            public void run() {
                SinkServer.this.shutdown("Server Shutdown");
            }
        });
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
    @Override
    public void shutdown(@Nullable String reason) {
        // TODO Fire shutdown event here, once I add it. :)
        server.logInfo("Server Shutdown: " + reason);
        server.getPlayerList().getPlayerList().stream()
            .forEach(player -> player.connection.kickPlayerFromServer(reason));
        server.stopServer();
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

    @Override
    public DedicatedServer getHandle() {
        return server;
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
            throw new UnsupportedOperationException();
        }

        @Override
        public int getQueryPort() {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public String getRemoteConsolePassword() {
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getRemoteConsolePort() {
            throw new UnsupportedOperationException();
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
            throw new UnsupportedOperationException();
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
            throw new UnsupportedOperationException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isQueryEnabled() {
            throw new UnsupportedOperationException();
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
