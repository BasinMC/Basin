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
package org.basinmc.sink

import org.apache.logging.log4j.LogManager
import java.io.IOException
import java.util.*

/**
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
object SinkVersion {

  val version: String

  val guava: String
  val log4j: String
  val commonsLang: String
  val gson: String
  val netty: String
  val findbugs: String

  init {
    val p = Properties()

    try {
      SinkVersion::class.java
          .getResourceAsStream("/sink-version.properties")
          .use(p::load)
    } catch (ex: IOException) {
      LogManager.getLogger(SinkVersion::class.java)
          .error("Could not load Sink version information: " + ex.message, ex)
    }

    version = p.getProperty("sink.version", "0.0.0")
    guava = p.getProperty("dependency.guava.version", "0.0.0")
    log4j = p.getProperty("dependency.log4j.version", "0.0.0")
    commonsLang = p.getProperty("dependency.commons.lang.version", "0.0.0")
    gson = p.getProperty("dependency.gson.version", "0.0.0")
    netty = p.getProperty("dependency.netty.version", "0.0.0")
    findbugs = p.getProperty("dependency.findbugs.version", "0.0.0")
  }
}
