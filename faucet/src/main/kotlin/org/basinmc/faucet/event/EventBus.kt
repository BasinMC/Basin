/*
 *  Copyright 2016 __0x277F <0x277F@gmail.com>
 *  and other copyright owners as documented in the project's IP log.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License&quot￼;
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.basinmc.faucet.event

import org.basinmc.faucet.event.handler.EventHandler
import java.util.function.Consumer

/**
 * Provides an event management system which will automatically post events to all registered
 * listeners.
 */
interface EventBus {

  /**
   * Posts an event to the event bus.
   *
   * Each handler configured to accept a supertype or equivalent type of event will receive the
   * event.
   *
   * @param event The even to post
   * @param <S> identifies the desired return state (as specified by the event listeners).
   */
  fun <E : Event<S>, S : Any> post(event: E): S

  /**
   * Subscribes an arbitrary event handler to this particular bus.
   *
   * @param handler a handler implementation.
   */
  fun subscribe(handler: EventHandler): Subscription

  /**
   * Scans the supplied object for compatible event handler methods and registers them with this
   * bus.
   *
   * Only method which bear the [org.basinmc.faucet.event.handler.Subscribe] annotation will
   * be considered by this method.
   *
   * @param listener an arbitrary object.
   */
  fun subscribe(listener: Any): Subscription

  /**
   * Subscribes the specified runnable functional to an arbitrary event.
   *
   * @param eventClass an event class.
   * @param runnable an arbitrary runnable which is invoked when the event is received.
   * @param <E> an event type.
   */
  fun <E : Event<*>> subscribe(eventClass: Class<E>,
      runnable: Runnable): Subscription

  /**
   * Subscribes the specified consumer functional to an arbitrary event.
   *
   * @param eventClass an event class.
   * @param consumer an event.
   * @param <E> an event type.
   */
  fun <E : Event<*>> subscribe(eventClass: Class<E>,
      consumer: Consumer<E>): Subscription
}
