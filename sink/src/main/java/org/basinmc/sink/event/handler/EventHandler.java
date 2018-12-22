package org.basinmc.sink.event.handler;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.basinmc.faucet.event.Event;
import org.basinmc.faucet.event.ExecutionContext;

/**
 * Represents an event subscription within the Sink event bus.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface EventHandler<E extends Event<S>, S extends Enum<S>> {

  /**
   * Retrieves the common ancestor event type which is accepted by this handler.
   *
   * @return an ancestor type.
   */
  @NonNull
  Class<E> getBaseEventType();

  /**
   * <p>Evaluates whether the specified implementation type is accepted by this handler.</p>
   *
   * <p>The return value of this method is expected to be constant (e.g. may not change dynamically
   * during execution).</p>
   *
   * @param type an implementation type.
   * @return true if accepted, false otherwise.
   */
  boolean accepts(@NonNull Class<? extends E> type);

  /**
   * Invokes the event handler implementation.
   *
   * @param ctx an execution context and the associated event state.
   * @param event an event instance.
   */
  void invoke(@NonNull ExecutionContext<S> ctx, @NonNull E event);
}
