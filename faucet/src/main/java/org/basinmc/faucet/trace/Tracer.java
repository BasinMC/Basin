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

import edu.umd.cs.findbugs.annotations.Nullable;
import org.basinmc.faucet.internal.warn.Volatile;

/**
 * Represents a chain of actions. Implementations should ideally not expose logic for
 * adding elements to this tracer.
 */
public interface Tracer extends Iterable<TraceNode> {

  /**
   * Retrieve the first element in this action chain, or null if the first action has
   * not been completed yet.
   *
   * @return the first node, or null if none exists
   */
  @Nullable
  TraceNode getRoot();

  /**
   * Retrieve the last element in this action chain, or null if no action has been
   * completed.
   *
   * @return the last node, or null if none exists
   */
  @Nullable
  TraceNode getHead();

  /**
   * Get the (1-indexed) length of this event chain.
   */
    int getLength();

  /**
   * Determine if this trace chain is complete. If {@code true} is returned, then that means
   * that the tracer will be finalized and no new nodes will be added. It also means that it
   * becomes eligible to release any bound resources, such as {@link TraceEvent}'s stored event.
   *
   * @return true if complete, false if the trace chain is incomplete.
   */
  boolean isComplete();

  /**
   * Attempt to synchronize the tracer up with its attached object. This should not be called
   * unless the tracer is executing asynchronously.
   */
  void synchronize();

  /**
   * If called within a method being tracked by this tracer, will attempt to resynchronize
   * this tracer with that step. A copy will be returned containing only the trace nodes
   * executed to that point, and the original will be unmodified.
   *
   * TODO think of a better name to avoid confusion with above
   */
  @Nullable
  @Volatile("Poor naming")
  Tracer resynchronize();
}
