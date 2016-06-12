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
package org.basinmc.faucet.event;

import org.basinmc.faucet.util.Priority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a method that is to be subscribed to events. Can annotate a class for this operation
 * to be applied to all methods within it annotated with this annotation upon construction.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EventSubscribe {
    /**
     * Provides a filter of events. If the event type specified in the method
     * parameter is abstract in some way, this filter can be used to limit the types
     * of events that will be passed to this.
     * @return An array of event types
     */
    Class<? extends Event>[] value() default Event.class;

    /**
     * The priority at which this event is called. Don't abuse this.
     * Seriously. If you abuse this, I will come after you. I will find you, and
     * I will kill you in the most brutal and agonizing way imaginable. I will literally
     * tear you limb from limb. So please, respect your software freedoms and use this
     * responsibly.
     * @return A priority that legitimately corresponds to the actions taken in the event handler.
     */
    Priority priority() default Priority.NORMAL;
}
