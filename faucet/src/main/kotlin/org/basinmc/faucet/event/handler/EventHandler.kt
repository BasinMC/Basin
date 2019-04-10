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
package org.basinmc.faucet.event.handler

import org.basinmc.faucet.event.Event
import org.basinmc.faucet.util.Priority
import javax.naming.event.EventContext
import kotlin.reflect.KClass

/**
 * Represents an arbitrary event handler which is notified of one or more event types within the
 * application.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
interface EventHandler : Comparable<EventHandler> {

  /**
   * Identifies the relative priority of this particular handler.
   */
  val priority: Priority
    get() = Priority.NORMAL

  /**
   * Evaluates whether this subscription accepts the given event type.
   *
   * Note that this method is expected to never change its return value throughout its lifetime as
   * this value will be used in order to generate cached subscription priority lists (e.g. if a
   * handler indicates its interest in a given event type it cannot start filtering based on
   * arbitrary conditions later on).
   *
   * @param eventType an arbitrary event type.
   * @return true if this event is accepted, false otherwise.
   */
  fun accepts(eventType: KClass<out Event<*>>): Boolean

  /**
   * Evaluates whether this subscription accepts the given event.
   *
   * @param ctx an arbitrary event context.
   * @return true if this event is accepted, false otherwise.
   */
  fun accepts(ctx: EventContext): Boolean

  /**
   * Executes the subscription specific handler logic.
   *
   * This method is only ever invoked if the event object is accepted by the handler via [ ][.accepts] and [.accepts].
   *
   * @param ctx an arbitrary event context.
   */
  operator fun invoke(ctx: EventContext)

  override fun compareTo(other: EventHandler) = this.priority.compareTo(other.priority)
}
