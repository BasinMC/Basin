package org.basinmc.sink.event;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.basinmc.faucet.event.Event;
import org.basinmc.faucet.event.EventBus;
import org.basinmc.sink.event.handler.EventHandler;
import org.basinmc.sink.event.handler.EventSubscriptionCache;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
@Service
public class SinkEventBus implements EventBus {

  private static final Logger logger = LogManager.getFormatterLogger(SinkEventBus.class);

  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Set<EventHandler<?, ?>> subscriptions = new HashSet<>();
  private final Map<Class<? extends Event<?>>, EventSubscriptionCache<?, ?>> cacheMap = new HashMap<>();

  /**
   * Clears the entire subscription cache in order to permit new registrations to be executed within
   * the next cycle.
   */
  public void clearCache() {
    this.lock.writeLock().lock();
    try {
      this.cacheMap.clear();
    } finally {
      this.lock.writeLock().unlock();
    }
  }

  /**
   * Retrieves an existing or builds a completely new event handler cache which stores all desired
   * handlers and their respective order of execution for a given event type.
   *
   * @param eventType an arbitrary event type.
   * @param <E> an event type.
   * @param <S> a state type.
   * @return a subscription cache.
   */
  @SuppressWarnings("unchecked")
  private <E extends Event<S>, S extends Enum<S>> EventSubscriptionCache<E, S> getOrBuildCache(
      @NonNull Class<E> eventType) {
    this.lock.readLock().lock();
    try {
      return (EventSubscriptionCache) this.cacheMap
          .computeIfAbsent(eventType, (e) -> this.buildCache(eventType));
    } finally {
      this.lock.readLock().unlock();
    }
  }

  /**
   * Rebuilds a new subscription cache which contains all desired handlers and their order of
   * execution.
   *
   * @param eventType an arbitrary event type.
   * @param <E> an event type.
   * @param <S> a state type.
   * @return a new subscription cache.
   */
  @SuppressWarnings("unchecked")
  private <E extends Event<S>, S extends Enum<S>> EventSubscriptionCache<E, S> buildCache(
      @NonNull Class<E> eventType) {
    logger.debug("Rebuilding handler cache for %s", eventType);

    var handlers = this.subscriptions.stream()
        .filter((handler) -> handler.getBaseEventType().isAssignableFrom(eventType))
        .filter((handler) -> handler.accepts((Class) eventType))
        .collect(Collectors.toList());

    return new EventSubscriptionCache<>(eventType, (Collection) handlers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <S extends Enum<S>> S post(@NonNull Event<S> event) {
    logger.debug("Posting %s event", event.getClass());

    var ctx = new SinkExecutionContext<>(event.getDefaultState());
    this.getOrBuildCache(event.getClass()).post(ctx, event);
    return ctx.getState();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void subscribe(@NonNull Object listener) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends Event<?>> void subscribe(@NonNull Class<E> eventClass,
      @NonNull Runnable runnable) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends Event<?>> void subscribe(@NonNull Class<E> eventClass,
      @NonNull Consumer<E> consumer) {
    throw new UnsupportedOperationException();
  }
}
