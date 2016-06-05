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

package org.basinmc.sink.plugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.plugin.PluginContext;
import org.basinmc.faucet.plugin.PluginLoader;
import org.basinmc.faucet.plugin.PluginMetadata;
import org.basinmc.sink.SinkServer;
import org.basinmc.sink.plugin.asm.MetadataClassVisitor;
import org.objectweb.asm.ClassReader;

import sun.misc.URLClassPath;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.jar.JarFile;

import javax.annotation.Nonnull;

public class SinkPluginLoader implements PluginLoader {
    public static final Logger logger = LogManager.getLogger("Sink Plugin Loader");

    private Queue<File> loadQueue;
    private Map<JarFile, PluginMetadata> transformQueue;
    private Map<JarFile, PluginMetadata> contextQueue;
    private SinkServer server;

    public SinkPluginLoader(SinkServer server) {
        this.server = server;
        this.loadQueue = new LinkedTransferQueue<>();
        this.transformQueue = new LinkedHashMap<>();
        this.contextQueue = new LinkedHashMap<>();
    }

    void loadPlugin(Path path) {
        File file = path.toFile();
        if (file.exists()) {
            loadQueue.add(file);
            logger.debug("Queued file " + file.getAbsolutePath() + " for loading.");
        } else {
            logger.warn("Attempted to load non-existent plugin file at " + path.toString());
        }
    }

    void loadPlugins(Path path) {
        File file = path.toFile();
        if (file.exists()) {
            if (file.isDirectory()) {
                if (file.listFiles() == null) return;
                Arrays.stream(file.listFiles()).forEachOrdered(f -> loadPlugin(f.toPath()));
            }
        } else {
            logger.warn("Attempted to load plugin files from non-existent directory " + path.toString());
        }
    }

    void performLoads() throws IOException {
        while (!loadQueue.isEmpty()) {
            File file = loadQueue.remove();
            if (file.getName().endsWith(".jar")) {
                JarFile jarfile = new JarFile(file);
                URLClassPath classpath = new URLClassPath(new URL[]{file.toURI().toURL()});
                jarfile.stream().filter(entry -> entry.getName().endsWith(".class")).forEach(entry -> {
                    try {
                        byte[] bytecode = classpath.getResource(entry.getName()).getBytes();
                        ClassReader reader = new ClassReader(bytecode);
                        reader.accept(new MetadataClassVisitor(this, metadata -> {
                            transformQueue.put(jarfile, metadata);
                        }), 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else if (file.isDirectory()) {
                // TODO
            }
        }
    }

    @Nonnull
    @Override
    public PluginContext createContext(@Nonnull Path packagePath) {
        return null;
    }
}
