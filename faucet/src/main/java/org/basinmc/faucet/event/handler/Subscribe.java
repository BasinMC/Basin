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
package org.basinmc.faucet.event.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.basinmc.faucet.event.MutableEvent;
import org.basinmc.faucet.util.Priority;
import org.basinmc.faucet.util.State;

/**
 * Annotates a method that is to be subscribed to events. Can annotate a class for this operation
 * to be applied to all methods within it annotated with this annotation upon construction.
 * This is also used to annotate synthetic {@link EventHandler} implementations internally.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Subscribe {

  /**
   * Declares the priority at which this handler is being called.
   *
   * Higher priority handlers will be called first within the queue and as such get the highest
   * authority over the event state.
   */
  @NonNull
  Priority priority() default Priority.NORMAL;

  /**
   * Indicates whether the annotated member will be notified of events which have been finalized.
   *
   * @see MutableEvent#isFinalized() for a detailed documentation on finalization.
   */
  boolean receiveFinalized() default false;

  /**
   * Indicates which state an event (of instance {@link org.basinmc.faucet.event.StatefulEvent})
   * has to be in at the time of posting in order to cause the framework to notify the annotated
   * member.
   *
   * Note: In addition to {@link State#ALLOW} and {@link State#DENY}, you may also use
   * {@link State#DEFAULT} in order to reduce the set of events to events which are currently in
   * their default state as well as {@link State#WILDCARD} to retrieve events from both sides.
   */
  @NonNull
  State receiveState() default State.ALLOW;
}
