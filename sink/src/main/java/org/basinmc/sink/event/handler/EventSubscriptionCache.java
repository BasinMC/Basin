package org.basinmc.sink.event.handler;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.basinmc.faucet.event.Event;
import org.basinmc.faucet.event.ExecutionContext;

/**
 * Caches all handlers for a given event types within their expected order of execution.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class EventSubscriptionCache<E extends Event<S>, S extends Enum<S>> {

  private final Class<E> eventType;
  private final List<EventHandler<E, S>> handlers = new ArrayList<>();

  public EventSubscriptionCache(@NonNull Class<E> eventType,
      @NonNull Collection<EventHandler<E, S>> handlers) {
    this.eventType = eventType;
    this.handlers.addAll(handlers);

    // TODO: sort priority
  }

  /**
   * Retrieves the event type for which this cache stores all respective handlers.
   *
   * @return an event type.
   */
  @NonNull
  public Class<E> getEventType() {
    return this.eventType;
  }

  /**
   * Notifies all handlers within this particular subscription cache.
   *
   * @param ctx an execution context for this event invocation.
   * @param event an event object.
   */
  public void post(@NonNull ExecutionContext<S> ctx, @NonNull E event) {
    this.handlers.forEach((handler) -> handler.invoke(ctx, event));
  }
}
