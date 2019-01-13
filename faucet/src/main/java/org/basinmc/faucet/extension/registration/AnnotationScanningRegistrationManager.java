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
package org.basinmc.faucet.extension.registration;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.stream.Collectors;
import org.basinmc.faucet.extension.Extension;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

/**
 * Provides an extension to the registration manager specification which permits the scanning of
 * annotated beans within newly initialized extensions.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class AnnotationScanningRegistrationManager<A extends Annotation, R extends Registration> extends
    ScanningRegistrationManager<R> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected Collection<R> scan(@NonNull Extension extension) {
    return extension.getContext().getBeansWithAnnotation(this.getAnnotation()).values().stream()
        .map(this::scanBean)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  /**
   * Retrieves the annotation which marks beans as qualified for automatic registration within this
   * manager.
   *
   * @return an annotation type.
   */
  @NonNull
  protected abstract Class<A> getAnnotation();

  /**
   * Scans a bean object for registrations.
   *
   * @param object a bean object.
   * @return a collection of registrations.
   */
  @NonNull
  protected Collection<R> scanBean(@NonNull Object object) {
    var annotation = AnnotationUtils.findAnnotation(object.getClass(), this.getAnnotation());
    Assert.notNull(annotation, "Annotation must be present on bean");

    return this.scanBean(annotation, object);
  }

  /**
   * @see #scanBean(Object)
   */
  @NonNull
  protected Collection<R> scanBean(@NonNull A annotation, @NonNull Object bean) {
    throw new UnsupportedOperationException("Scanning is not implemented");
  }
}
