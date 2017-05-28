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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;

/**
 * Stores compile-time information about the server.
 */
public final class FaucetVersion {

  public static final String MINECRAFT_VERSION;
  public static final String API_VERSION;
  public static final String API_VERSION_EXTRA;

  static {
    Properties properties = new Properties();

    try (InputStream inputStream = FaucetVersion.class
        .getResourceAsStream("/faucet-version.properties")) {
      properties.load(inputStream);
    } catch (IOException ex) {
      LogManager.getLogger(FaucetVersion.class)
          .error("Could not load Faucet version information: " + ex.getMessage(), ex);
    }

    API_VERSION = properties.getProperty("faucet.version", "0.0");
    API_VERSION_EXTRA = properties.getProperty("faucet.version.extra", "");
    MINECRAFT_VERSION = properties.getProperty("minecraft.version", "0.0");
  }

  private FaucetVersion() {
  }
}
