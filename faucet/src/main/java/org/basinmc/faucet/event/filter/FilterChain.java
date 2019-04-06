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
package org.basinmc.faucet.event.filter;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;
import org.basinmc.faucet.event.Event;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Provides an event filter chain implementation which handles the discovery and initialization of
 * custom filter types.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class FilterChain {

  private final List<EventFilter<?>> chain = new ArrayList<>();

  public FilterChain(AnnotatedElement element) {
    var annotations = AnnotationUtils.getAnnotations(element);
    if (annotations == null) {
      return;
    }

    for (var annotation : annotations) {
      var filterSpec = AnnotationUtils.getAnnotation(annotation, Filter.class);
      if (filterSpec == null) {
        continue;
      }

      this.append(annotation, filterSpec);
    }
  }

  @SuppressWarnings("unchecked")
  private void append(Annotation annotation, Filter spec) {
    MethodHandle constructor;
    try {
      constructor = MethodHandles.lookup()
          .findConstructor(spec.value(), MethodType.methodType(void.class));
    } catch (NoSuchMethodException ex) {
      throw new IllegalArgumentException(
          "Illegal filter specification: Missing no-args constructor", ex);
    } catch (IllegalAccessException ex) {
      throw new IllegalArgumentException(
          "Illegal filter specification: Inaccessible no-args constructor", ex);
    }

    EventFilter filter;
    try {
      filter = (EventFilter<?>) constructor.invoke();
    } catch (Throwable ex) {
      throw new IllegalArgumentException("Invalid filter specification: Failed to construct filter",
          ex);
    }

    filter.initialize(annotation);
    this.chain.add(filter);
  }

  /**
   * Evaluates whether this filter chain matches the given event.
   *
   * @param event an event.
   * @param <E> an event type.
   * @return true if matches (e.g. passes the chain's restrictions), false otherwise.
   */
  public <E extends Event<?>> boolean matches(E event) {
    return this.chain.stream()
        .allMatch((filter) -> filter.matches(event));
  }
}
