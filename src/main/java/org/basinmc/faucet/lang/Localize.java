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
package org.basinmc.faucet.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a field or a local variable to signify that string values that each
 * might represent will be transformed (at plugin load time) into an unlocalized
 * string, which will be localized during outgoing packet processing for
 * significant packets, such as chat, item metadata (including name and lore), etc.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.TYPE})
public @interface Localize {
    /**
     * The key for which localization will be attempted. By default this will be
     * filled by the string being targeted.
     * @return A string representation of the key
     */
    String value() default "$PROVIDED$";

    /**
     * If no valid localization for the player is found, the locale to use. If this
     * is unable to function for whatever reason, the player will receive the key
     * instead.
     * @return A locale string
     */
    String defaultLocale() default "en_US";

    /**
     * A directory path where locale files (which <i>must</i> follow the format of
     * "en_US.lang" can be found. If the given path is checked and a locale file is
     * not found, the {@link Localize#defaultLocale()} will be attempted instead,
     * then the key will be sent instead.
     * @return A locale string
     */
    String localePath() default "/lang/";
}
