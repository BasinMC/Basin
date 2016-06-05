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

package org.basinmc.faucet.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a main plugin class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Plugin {
    /**
     * {@link org.basinmc.faucet.plugin.PluginMetadata}
     */
    String id();

    /**
     * {@link org.basinmc.faucet.plugin.PluginMetadata}
     */
    String name() default "##ID##";

    /**
     * {@link org.basinmc.faucet.plugin.PluginMetadata}
     */
    String desc() default "";

    /**
     * {@link org.basinmc.faucet.plugin.PluginMetadata}
     */
    String version();

    /**
     * {@link org.basinmc.faucet.plugin.PluginMetadata}
     */
    String author() default "Anonymous";

    /**
     * {@link org.basinmc.faucet.plugin.PluginMetadata} A list of dependency strings that represent
     * version ranges and dependency plugin IDs. The format for each is "{plugin-id}@{version}". One
     * can append a second version string after the first and separate the two with a hyphen to
     * denote an (doubly-inclusive) version range. Alternatively, one can append to the version
     * string a plus sign ("+") or a minus sign ("-") to denote any version greater than or less
     * than (respectively) the version supplied.
     *
     * @return An array of version strings conforming to the above specification.
     */
    String[] dependencies() default {};

    /**
     * {@link org.basinmc.faucet.plugin.PluginMetadata} Functions the same as {@link
     * Plugin#dependencies()}
     */
    String[] softDependencies() default {};
}
