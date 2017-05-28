/*
 * Copyright 2017 Hex <hex@hex.lc>
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
package org.basinmc.faucet.internal.event;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.basinmc.faucet.internal.util.StringParsable;

/**
 * Annotates a field in an event class to expose it and map it to a specific filter annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FilterExpose {

  /**
   * The filter annotation being mapped.
   */
  Class<? extends Annotation> value();

  /**
   * A mapper for the filter annotation to use. May be omitted in most cases.
   */
  Class<? extends StringParsable> mapper() default StringParsable.class;

  /**
   * A value to override the field name.
   */
  String name() default "";

  /**
   * Use to extract a specific value from an object field. Specifies the field name.
   */
  String extract() default "";
}