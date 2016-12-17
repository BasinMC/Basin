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

import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class SinkVersion {
    public static final String IMPLEMENTATION_VERSION;

    public static final String GUAVA_VERSION;
    public static final String LOG4J_VERSION;
    public static final String COMMONS_LANG_VERSION;
    public static final String GSON_VERSION;
    public static final String NETTY_VERSION;
    public static final String FINDBUGS_VERSION;
    public static final String OSGI_VERSION;

    static {
        Properties p = new Properties();

        try (InputStream inputStream = SinkVersion.class.getResourceAsStream("/sink-version.properties")) {
            p.load(inputStream);
        } catch (IOException ex) {
            LogManager.getLogger(SinkVersion.class).error("Could not load Sink version information: " + ex.getMessage(), ex);
        }

        IMPLEMENTATION_VERSION = p.getProperty("sink.version", "0.0.0");
        GUAVA_VERSION = p.getProperty("dependency.guava.version", "0.0.0");
        LOG4J_VERSION = p.getProperty("dependency.log4j.version", "0.0.0");
        COMMONS_LANG_VERSION = p.getProperty("dependency.commons.lang.version", "0.0.0");
        GSON_VERSION = p.getProperty("dependency.gson.version", "0.0.0");
        NETTY_VERSION = p.getProperty("dependency.netty.version", "0.0.0");
        FINDBUGS_VERSION = p.getProperty("dependency.findbugs.version", "0.0.0");
        OSGI_VERSION = p.getProperty("dependency.osgi.version", "0.0.0");
    }

    private SinkVersion() {
    }
}
