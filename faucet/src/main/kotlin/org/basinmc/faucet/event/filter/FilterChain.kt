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

import org.basinmc.faucet.event.Event
import org.springframework.core.annotation.AnnotationUtils
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.lang.reflect.AnnotatedElement

/**
 * Provides an event filter chain implementation which handles the discovery and initialization of
 * custom filter types.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
class FilterChain(element: AnnotatedElement) {

  private val chain = mutableListOf<EventFilter<*>>()

  init {
    AnnotationUtils.getAnnotations(element)?.let { annotations ->
      for (annotation in annotations) {
        val filterSpec = AnnotationUtils.getAnnotation(annotation, Filter::class.java) ?: continue

        this.append(annotation, filterSpec)
      }
    }
  }

  private fun append(annotation: Annotation, spec: Filter) {
    val constructor: MethodHandle
    try {
      constructor = MethodHandles.lookup()
          .findConstructor(spec.value.java, MethodType.methodType(Void.TYPE))
    } catch (ex: NoSuchMethodException) {
      throw IllegalArgumentException(
          "Illegal filter specification: Missing no-args constructor", ex)
    } catch (ex: IllegalAccessException) {
      throw IllegalArgumentException(
          "Illegal filter specification: Inaccessible no-args constructor", ex)
    }

    val filter = try {
      @Suppress("UNCHECKED_CAST")
      constructor.invoke() as EventFilter<Annotation>
    } catch (ex: Throwable) {
      throw IllegalArgumentException("Invalid filter specification: Failed to construct filter",
          ex)
    }

    filter.initialize(annotation)
    this.chain += filter
  }

  /**
   * Evaluates whether this filter chain matches the given event.
   *
   * @param event an event.
   * @param <E> an event type.
   * @return true if matches (e.g. passes the chain's restrictions), false otherwise.
  </E> */
  fun <E : Event<*>> matches(event: E): Boolean {
    return this.chain.stream()
        .allMatch { filter -> filter.matches(event) }
  }
}
