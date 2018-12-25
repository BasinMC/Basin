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
package org.basinmc.faucet.extension.annotation;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

/**
 * Marks the annotated component as an exported service which is made available to other plugins.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
@Service
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
public @interface ExportedService {

  /**
   * @see #version()
   */
  @AliasFor("version")
  String value() default "";

  /**
   * Defines the parent service interface which is implemented by this service export.
   *
   * @return an arbitrary base class.
   */
  @NonNull
  Class<?> baseClass() default Void.class;

  /**
   * Specifies the specification version which is implemented by this service.
   *
   * @return a version number.
   */
  @AliasFor("value")
  String version() default "";
}
