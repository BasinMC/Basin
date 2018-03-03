/*
 * Copyright 2016 Hex <hex@hex.lc>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License&quot￼;
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
 *
 */
package org.basinmc.faucet.trace;

import java.util.function.Consumer;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.basinmc.faucet.event.Event;

/**
 * Represents a node in a trace chain backed by either an event handler or an event trigger.
 *
 * @param <T> the event type
 */
public interface TraceEvent<T extends Event> extends TraceMethod {

  /**
   * Checks if this node is the result of an event being posted or being handled.
   *
   * @return true if posted, false if handled
   */
  boolean isTrigger();

  /**
   * Gets the most specific possible type of event which is being handled or triggered.
   *
   * @return a class extending {@link Event}
   */
  @NonNull
  Class<? extends T> getEventType();

  /**
   * Get a snapshot of the event at the time this node was constructed. The snapshot returned
   * will not have changes reflected in this node's stored snapshot.
   *
   * @return an event
   */
  @NonNull
  T getSnapshot();

  /**
   * Retrieve the event in its current state. Note that this event is stored in a weakly
   * referenced manner, so by the time this method is called the event may have already
   * been garbage-collected.
   *
   * @return the current event, or null if it is no longer valid
   */
  @Nullable
  T getEvent();

  /**
   * Look up a {@link Consumer} object corresponding to the given event handler. If the consumer is
   * not cached, this will cause a method handle lookup by way of {@link
   * java.lang.invoke.LambdaMetafactory}, which should be taken into account for performance
   * reasons.
   */
  @NonNull
  Consumer<T> lookup();
}
