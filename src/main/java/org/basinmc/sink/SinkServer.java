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

import org.basinmc.faucet.Server;

import javax.annotation.Nonnull;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class SinkServer implements Server {
    private final MinecraftServer server;

    public SinkServer(MinecraftServer server) {
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
}
