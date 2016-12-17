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

import com.google.common.collect.Iterators;
import com.google.common.reflect.ClassPath;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import net.minecraft.init.Bootstrap;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.datafix.DataFixesManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.basinmc.faucet.FaucetVersion;
import org.basinmc.faucet.Handled;
import org.basinmc.faucet.Server;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class SinkServer implements Server, Handled<DedicatedServer> {
    private static final Logger logger = LogManager.getFormatterLogger(SinkServer.class);
    private static final Options OPTIONS = new Options()
            .addOption(Option.builder().longOpt("help").desc("Prints this help message before gracefully shutting the application down.").build())
            .addOption(Option.builder("b").longOpt("bundle-directory").hasArg().desc("Declares the directory to store plugin and library bundles in.").build())
            .addOption(Option.builder("c").longOpt("cache-directory").hasArg().desc("Declares the directory to store framework cache files in.").build())
            .addOption(Option.builder().longOpt("debug").desc("Increases the log level to include debug messages (this generates a lot of messages and should not be used in production environments).").build())
            .addOption(Option.builder().longOpt("enable-development-features").desc("Enables features which are designed to assist developers with creating plugins.").build())
            .addOption(Option.builder().longOpt("disable-easter-eggs").desc("Disables all easter eggs which have been introduced by the Basin team.").build())
            .addOption(Option.builder().longOpt("disable-gui").desc("Prevents the server GUI to be displayed (this setting is automatically activated in headless environments).").build())
            .addOption(Option.builder().longOpt("disable-performance-optimizations").desc("Disables all Sink performance optimizations.").build())
            .addOption(Option.builder().longOpt("server-port").hasArg().desc("Declares the port this server should listen on (defaults to 25565, may be overridden in server.properties)").build())
            .addOption(Option.builder().longOpt("version").desc("Prints the application and API version before gracefully shutting the application down.").build())
            .addOption(Option.builder("w").longOpt("world-directory").hasArg().desc("Declares the directory to store the worlds of this server in (default: worlds).").build());

    // this is pretty ugly but it'll contribute to keeping our patches a little smaller
    private static SinkServer instance;

    private final DedicatedServer server;
    private final Server.Configuration configuration = new Configuration();
    private final Framework framework;

    private SinkServer(@Nonnull DedicatedServer server) throws ClassNotFoundException, IllegalStateException {
        this.server = server;
        this.framework = buildFrameworkInstance();

        Runtime.getRuntime().addShutdownHook(new Thread("Sink Shutdown Thread") {
            @Override
            public void run() {
                SinkServer.this.shutdown("Server Shutdown");
            }
        });
    }

    private static void buildExposedPackages(@Nonnull StringBuilder builder, @Nonnull String topLevelPackageName, @Nonnull String v, @Nonnull Predicate<String> filter) throws IOException {
        // while OSGi does specify Semantic Version as its versioning scheme of choice, it does not
        // seem to understand dashes as part of these versions and thus we will replace any
        // occurrences of dashes with dots in order to comply with its specification
        // this is more of a hack but should not actually have any effects on the behavior of the
        // framework
        final String version;

        if (v.contains("-")) {
            version = v.replace('-', '.');
        } else {
            version = v;
        }

        ClassPath.from(SinkServer.class.getClassLoader()).getTopLevelClassesRecursive(topLevelPackageName).stream()
                .map(ClassPath.ClassInfo::getPackageName)
                .distinct()
                .filter(filter)
                .forEach((p) -> {
                    if (builder.length() != 0) {
                        builder.append(',');
                    }

                    builder.append(p).append(";").append(version);
                    logger.debug("Exposing package: %s", p + "; version=" + version);
                });
    }

    private static void buildExposedPackages(@Nonnull StringBuilder builder, @Nonnull String topLevelPackageName, @Nonnull String v) throws IOException {
        buildExposedPackages(builder, topLevelPackageName, v, (p) -> !p.contains("internal"));
    }

    /**
     * Builds a specialized framework configuration.
     */
    @Nonnull
    private static Map<String, String> buildFrameworkConfiguration() {
        Map<String, String> cnf = new HashMap<>();
        {
            cnf.put(Constants.FRAMEWORK_STORAGE, "cache");
            cnf.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);

            final StringBuilder exposedPackages = new StringBuilder();
            try {
                buildExposedPackages(exposedPackages, "org.basinmc.faucet", FaucetVersion.API_VERSION);
                buildExposedPackages(exposedPackages, "org.basinmc.sink", SinkVersion.IMPLEMENTATION_VERSION);
                buildExposedPackages(exposedPackages, "net.minecraft", FaucetVersion.MINECRAFT_VERSION);

                // FIXME: This part is more than messy and does not actually respect internal
                // packages as declared by the respective libraries
                // as such it should be considered to move the plugin framework to a bootstrap
                // library (e.g. flange) which will provide an appropriate runtime and proper
                // integration with the Basin specific OSGi extensions in the future
                // this would also allow us to restart the entire server on demand without requiring
                // an external script to do so
                // in addition such an interface would open up an entire new ecosystem to authors
                // of third party integrations since they could easily provide their own console
                // implementations and the like without having to fork Basin itself
                buildExposedPackages(exposedPackages, "com.google.common", SinkVersion.GUAVA_VERSION);
                buildExposedPackages(exposedPackages, "org.apache.logging.log4j", SinkVersion.LOG4J_VERSION, (p) -> !p.contains("core"));
                buildExposedPackages(exposedPackages, "org.apache.commons.lang3", SinkVersion.COMMONS_LANG_VERSION);
                buildExposedPackages(exposedPackages, "com.google.gson", SinkVersion.GSON_VERSION);
                buildExposedPackages(exposedPackages, "io.netty", SinkVersion.NETTY_VERSION);
                buildExposedPackages(exposedPackages, "javax.annotation", SinkVersion.FINDBUGS_VERSION);
                buildExposedPackages(exposedPackages, "org.osgi", SinkVersion.OSGI_VERSION);
            } catch (IOException ex) {
                throw new RuntimeException("Could not discover application packages: " + ex.getMessage(), ex);
            }
            cnf.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, exposedPackages.toString());
        }
        return cnf;
    }

    /**
     * Locates and instantiates an implementation of {@link FrameworkFactory} using the
     * {@link ServiceLoader} system.
     *
     * @throws ClassNotFoundException when no implementation is available within the Class-Path.
     * @throws IllegalStateException  when more than one implementation is available within the
     *                                Class-Path.
     */
    @Nonnull
    private static FrameworkFactory buildFrameworkFactory() throws ClassNotFoundException, IllegalStateException {
        try {
            return Iterators.getOnlyElement(ServiceLoader.load(FrameworkFactory.class).iterator());
        } catch (NoSuchElementException ex) {
            throw new ClassNotFoundException("No valid implementation of org.osgi.framework.launch.FrameworkFactory located within Class-Path");
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("More than one implementation of org.osgi.framework.launch.FrameworkFactory located within Class-Path");
        }
    }

    /**
     * Builds a new framework instance using a specialized configuration for Basin.
     *
     * @throws ClassNotFoundException when no implementation is available within the Class-Path.
     * @throws IllegalStateException  when more than one implementation is available within the
     *                                Class-Path.
     */
    @Nonnull
    private static Framework buildFrameworkInstance() throws ClassNotFoundException, IllegalStateException {
        FrameworkFactory factory = buildFrameworkFactory();
        return factory.newFramework(buildFrameworkConfiguration());
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
     * Provides a replacement entry point for the server in order to gain complete control over the
     * command line arguments passed to Sink.
     *
     * This method is to be kept in sync with
     * {@link net.minecraft.server.MinecraftServer#main(String[])} to maintain compatibility.
     *
     * @param arguments an array of command line arguments.
     */
    public static void main(@Nonnull String[] arguments) {
        try {
            main(new DefaultParser().parse(OPTIONS, arguments));
        } catch (ParseException ex) {
            System.err.println("Invalid Input: " + ex.getMessage());
            System.err.println();
            printHelp();
            System.exit(-1);
        }
    }

    /**
     * Provides a simplified internal entry point to decouple command line parsing from the actual
     * main method implementation.
     */
    private static void main(@Nonnull CommandLine cmd) {
        // since this output is intended to be used by bash scripts or other third party software
        // in order to detect compatibility, we'll give this argument the highest possible priority
        if (cmd.hasOption("version")) {
            System.out.println(FaucetVersion.MINECRAFT_VERSION);
            System.out.println(FaucetVersion.API_VERSION);
            return;
        }

        System.out.println("Basin Sink for Minecraft " + FaucetVersion.MINECRAFT_VERSION + " (implementing Faucet " + FaucetVersion.API_VERSION + "+" + FaucetVersion.API_VERSION_EXTRA + ")");
        System.out.println("Provided under the terms of the Apache License, Version 2.0");

        // print the help message before starting the server since we won't need its capabilities
        // in order to notify users of our very own functionality
        if (cmd.hasOption("help")) {
            printHelp();
            System.exit(0);
        }

        if (cmd.hasOption("debug")) {
            logger.info("Enabled debug logging");

            // Note this is a hack since it makes use of log4j internals - May need updating when
            // the core version is updated
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            org.apache.logging.log4j.core.config.Configuration cnf = ctx.getConfiguration();

            LoggerConfig config = cnf.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
            config.setLevel(Level.DEBUG);
            ctx.updateLoggers();
        }

        logger.info("Initializing Minecraft %s", FaucetVersion.MINECRAFT_VERSION);
        Bootstrap.register(); // apparently this is how the registries work ... don't question it

        // log some environment information
        logger.info("Running on Java v%s supplied by %s", System.getProperty("java.version", "Unknown"), System.getProperty("java.vendor"));

        if (cmd.hasOption("disable-gui") || GraphicsEnvironment.isHeadless()) {
            logger.info("Server GUI has been disabled or is unavailable in this environment");
        }

        // TODO: We probably want to replace some of these implementations in order to allow plugins
        // to properly replace or alter their behavior
        YggdrasilAuthenticationService var15 = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
        MinecraftSessionService var16 = var15.createMinecraftSessionService();
        GameProfileRepository var17 = var15.createProfileRepository();
        PlayerProfileCache var18 = new PlayerProfileCache(var17, new File(".", "usercache.json"));

        DedicatedServer server = new DedicatedServer(new File("."), DataFixesManager.createFixer(), var15, var16, var17, var18);
        server.setFolderName(cmd.hasOption("world-directory") ? "world-directory" : "world");

        if (cmd.hasOption("server-port")) {
            server.setServerPort(Integer.parseUnsignedInt(cmd.getOptionValue("server-port")));
        }

        // TODO: Bonus chests?

        if (!cmd.hasOption("disable-gui") && !GraphicsEnvironment.isHeadless()) {
            server.setGuiEnabled();
        } else {
            logger.info("Server GUI has been disabled or is not available within the current environment");
        }

        try {
            instance = new SinkServer(server);
            instance.start();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Invalid Class-Path: " + ex.getMessage(), ex);
        }
    }

    /**
     * Prints the command line argument help to the standard program output.
     */
    private static void printHelp() {
        new HelpFormatter().printHelp("java -jar Sink.jar", OPTIONS, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown(@Nullable String reason) {
        // TODO Fire shutdown event here, once I add it. :)
        server.logInfo("Server Shutdown: " + reason);
        server.getPlayerList().getPlayerList().forEach(player -> player.connection.kickPlayerFromServer(reason));
        server.stopServer();
    }

    /**
     * Handles the startup phase of the server.
     */
    private void start() {
        try {
            logger.info("Starting plugin framework");
            this.framework.start();
            logger.debug("Plugin framework has been initialized");
        } catch (BundleException ex) {
            logger.error("Failed to initialize plugin framework: " + ex.getMessage(), ex);
            logger.error("Cannot continue - Server is shutting down");
            System.exit(-1);
        }

        logger.info("Starting Minecraft Server");
        this.server.startServerThread();
        logger.debug("Server thread has been started");
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Path getBaseDirectory() {
        // TODO: Add support for a variable base directory since this might be useful in order to share jars
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

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Framework getPluginFramework() {
        return this.framework;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
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
