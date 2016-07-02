/*
 *  Copyright 2016 __0x277F <0x277F@gmail.com>
 *  and other copyright owners as documented in the project's IP log.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License&quot￼;
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.basinmc.sink;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.plugin.error.PluginLoaderException;
import org.basinmc.sink.plugin.SinkPluginManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class Launcher implements Runnable {
    private static Logger logger = LogManager.getLogger("Sink Launcher");
    private Path serverDirectory = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().getFile()).toPath();
    private final String[] programArgs;

    public static void main(String[] args) {
        new Launcher(args).run();
    }

    public Launcher(String[] args) {
        this.programArgs = args;
    }

    @Override
    public void run() {
        // Load plugins before anything else.
        SinkPluginManager pluginManager = new SinkPluginManager(serverDirectory.resolve("data"));
        SinkPluginManager.LaunchingClassLoader classLoader = new SinkPluginManager.LaunchingClassLoader(pluginManager, Thread.currentThread().getContextClassLoader());
        try {
            final Class<?> mcserverClass = classLoader.loadClass("net.minecraft.server.MinecraftServer");
            mcserverClass.getDeclaredMethod("main", String[].class).invoke(null, (Object[]) programArgs);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            pluginManager.installDirectory(serverDirectory.resolve("plugins"), ex -> {
                logger.error("Exception occurred during loading of plugins: ");
                logger.error(ex);
                return true;
            });
        } catch (PluginLoaderException e) {
            e.printStackTrace();
        }

    }
}
