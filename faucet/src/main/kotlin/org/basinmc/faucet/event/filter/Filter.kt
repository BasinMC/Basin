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
package org.basinmc.faucet.event.filter

import kotlin.reflect.KClass

/**
 * Marks the annotated annotation as a filter configuration which is appended to the filter chain of
 * annotated subscribers.
 *
 * This is a meta-annotation.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Filter(

    /**
     * Identifies the filter implementation which is referenced by this annotation.
     *
     * Note that the first and only generic attribute is expected to match the annotation as it is
     * passed to [EventFilter.initialize] to pass potential configuration
     * parameters.
     *
     * @return a filter implementation.
     */
    val value: KClass<out EventFilter<*>>)
