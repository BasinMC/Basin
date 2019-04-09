/*
 * Copyright 2018 Hex <hex@hex.lc>
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

package org.basinmc.faucet.event.filter

import org.basinmc.faucet.internal.event.InceptionDefault
import kotlin.reflect.KClass

/**
 * Requires that the event be posted from a specific class or classloader.
 */
@Repeatable
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER)
annotation class InceptionFilter(
    /**
     * Requires that the event be posted from the specified class. If left unspecified,
     * [.classloader] must be specified.
     */
    val type: KClass<*> = InceptionDefault::class,

    /**
     * Requires that the event be posted from the specified class. If left unspecified, [.type]
     * must be specified.
     */
    val classloader: KClass<*> = InceptionDefault::class)
