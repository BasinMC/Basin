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
 * Annotates a method or type to denote that it should be removed at startup
 * if the specified version is not the same as the one the server is running.
 * If a suitable replacement is given, it will be dropped in place of the
 * annotated body. Otherwise, the body will be fully removed and any
 * references to it will cause an error.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
public @interface VersionOnly {
    /**
     * The minecraft version that must be present. Examples:
     * <p>1.10.2</p>, <p>1.9-1.9.4</p>
     * @return A version string or range.
     */
    String version();

    /**
     * All alternative classes and their corresponding versions.
     * If left blank, the body will not be replaced.
     * @return An array of alternatives.
     */
    Alternative[] alt() default {};
}
