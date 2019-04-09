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
package org.basinmc.faucet.extension.registration

import org.basinmc.faucet.extension.Extension
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.util.Assert

/**
 * Provides an extension to the registration manager specification which permits the scanning of
 * annotated beans within newly initialized extensions.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
abstract class AnnotationScanningRegistrationManager<A : Annotation, R : Registration> :
    ScanningRegistrationManager<R>() {

  /**
   * Retrieves the annotation which marks beans as qualified for automatic registration within this
   * manager.
   *
   * @return an annotation type.
   */
  protected abstract val annotation: Class<A>

  /**
   * {@inheritDoc}
   */
  override fun scan(extension: Extension): Collection<R> {
    val ctx = extension.context ?: return emptyList()

    return ctx
        .getBeansWithAnnotation(this.annotation)
        .flatMap(this::scanBean)
        .toList()
  }

  /**
   * Scans a bean object for registrations.
   *
   * @param bean a bean object.
   * @return a collection of registrations.
   */
  protected fun scanBean(bean: Any): Collection<R> {
    val annotation = AnnotationUtils.findAnnotation(bean.javaClass, this.annotation)
    Assert.notNull(annotation, "Annotation must be present on bean")

    return this.scanBean(annotation!!, bean)
  }

  /**
   * @see .scanBean
   */
  protected fun scanBean(annotation: A, bean: Any): Collection<R> {
    TODO("Scanning is not yet implemented")
  }
}
