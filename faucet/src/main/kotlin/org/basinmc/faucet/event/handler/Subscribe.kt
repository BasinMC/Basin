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
package org.basinmc.faucet.event.handler

import org.basinmc.faucet.event.Event
import org.basinmc.faucet.event.ExecutionContext
import org.basinmc.faucet.event.StatelessEvent
import org.basinmc.faucet.util.Priority
import org.basinmc.faucet.util.State
import kotlin.reflect.KClass

/**
 * Annotates a method that is to be subscribed to events. Can annotate a class for this operation to
 * be applied to all methods within it annotated with this annotation upon construction.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER)
annotation class Subscribe(

    /**
     * Specifies the event type to bind this particular handler to.
     *
     * @return an event type.
     */
    val eventType: Array<KClass<out Event<*>>> = [DefaultEvent::class],

    /**
     * Declares the priority at which this handler is being called.
     *
     * Higher priority handlers will be called first within the queue and as such get the highest
     * authority over the event state.
     */
    val priority: Priority = Priority.NORMAL,

    /**
     * Indicates which state an event (of instance [ExecutionContext]) has to be in at the time
     * of posting in order to cause the framework to notify the annotated member.
     *
     * Note: In addition to [State.ALLOW] and [State.DENY], you may also use [ ][State.DEFAULT] in order to reduce the set of events to events which are currently in their
     * default state as well as [State.WILDCARD] to retrieve events from both sides.
     */
    val receiveState: State = State.WILDCARD) {

  class DefaultEvent private constructor() : StatelessEvent
}
