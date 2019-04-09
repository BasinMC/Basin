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

import org.apache.logging.log4j.LogManager
import org.basinmc.faucet.util.Version
import java.io.IOException
import java.util.*

/**
 * Stores compile-time information about the server.
 */
object FaucetVersion {

  val minecraft: String
  val api: Version

  init {
    val properties = Properties()

    try {
      FaucetVersion::class.java
          .getResourceAsStream("/faucet-version.properties")
          .use(properties::load)
    } catch (ex: IOException) {
      LogManager.getLogger(FaucetVersion::class.java)
          .error("Could not load Faucet version information: " + ex.message, ex)
    }

    api = properties.getProperty("faucet.version", null)
        ?.let { Version(it) } ?: Version.default // TODO: May throw
    minecraft = properties.getProperty("minecraft.version", "0.0")
  }
}
