/*
 * Copyright 2018 Johannes Donath  <johannesd@torchmind.com>
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
package org.basinmc.faucet.extension.annotation

import org.springframework.stereotype.Service
import kotlin.reflect.KClass

/**
 * Marks the annotated component as an exported service which is made available to other plugins.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
@Service
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS, AnnotationTarget.FILE,
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ExportedService(

    /**
     * Defines the parent service interface which is implemented by this service export.
     *
     * @return an arbitrary base class.
     */
    val baseClass: KClass<*> = Void::class,

    /**
     * Specifies the specification version which is implemented by this service.
     *
     * @return a version number.
     */
    val version: String = "")
