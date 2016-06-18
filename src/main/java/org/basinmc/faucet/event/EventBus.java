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

import java.lang.reflect.Method;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Manages events in a FIFO manner. Any implementation <i>should</i>
 * be thread-safe.
 */
@ThreadSafe
public interface EventBus {
    /**
     * Subscribes an {@link EventHandler} to the type of event specified.
     * Until it is unsubscribed, it will continue to receive notifications
     * of all events corresponding to the specified types.
     * @param handler The handler to be registered
     * @param <T> The type of event that will be subscribed to.
     * @param eventType If the type of event is abstract, what types to allow through the filter.
     * @return Whether the registration was successful.
     */
    <T extends Event> boolean subscribe(@Nonnull EventHandler<T> handler, @Nonnull Class<? extends Event>[] eventType);

    /**
     * Unsubscribes an event handler.
     * @param handler The handler to remove
     * @param <T> The type of event the handler is designed to catch
     * @return Whether the handler was successfully removed. This would return false if the handler was not already subscribed.
     */
    <T extends Event> boolean unsubscribe(@Nullable EventHandler<T> handler);

    /**
     * Unsubscribes all event handlers that are designed to listen to a particular event.
     * @param eventType The type of event to be removed. May be an abstract type that would remove all handlers of all events subclassing it.
     * @param <T> The type of event to be removed
     * @return A collection of all removed event handlers
     */
    <T extends Event> Collection<EventHandler<? extends T>> unsubscribeAll(@Nonnull Class<T> eventType);

    /**
     * Subscribes all methods annotated with {@link EventSubscribe} within an object
     * to this event bus
     * @param holder The object holding annotated methods
     * @return Whether all subscriptions completed successfully
     */
    boolean subscribe(@Nonnull Object holder);

    /**
     * Creates a new instance of the type and subscribes it to this
     * event bus (see {@link EventBus#subscribe(Object)}.
     * @param type The type to instantiate
     * @return Whether everything completed successfully.
     */
    boolean subscribe(@Nonnull Class<?> type);

    /**
     * Unsubscribes all event handlers contained within this object.
     * See {@link EventBus#subscribe(Object)}.
     * @param holder The object that holds methods that will be unregistered.
     * @return Whether everything was successful.
     */
    boolean unsubscribe(@Nullable Object holder);

    /**
     * Unsubscribes any instances of the given type and all their enclosed methods.
     * @param type The type to unsubscribe.
     * @return Whether unsubscription was successful.
     */
    boolean unsubscribe(@Nullable Class<?> type);

    /**
     * Creates a wrapper for a method and registers it as an event handler.
     * This is mostly for internal use.
     * @param <T> The type of event the method will listen to.
     * @param holder An instance of the object holding the method.
     * @param method The method to wrap
     * @return Whether the wrapper was successfully generated and subscription was successful.
     */
    <T extends Event> boolean subscribe(@Nonnull Object holder, @Nonnull Method method);

    /**
     * Checks to see if a handler object is registered.
     * @param handler The object to check
     * @param <T> The type of event the handler is listening to
     * @return Whether the handler is registered
     */
    <T extends Event> boolean isRegistered(@Nonnull EventHandler<T> handler);

    /**
     * Gets all handlers that would receive a particular event.
     * @param eventType The type of event that would be tested.
     * @param <T> The type of event.
     * @return A collection of all event handlers that are subscribed to this event type.
     */
    <T extends Event> Collection<EventHandler<? super T>> getHandlers(@Nullable Class<T> eventType);

    /**
     * Posts an event to the event bus. Each handler configured to
     * accept a supertype or equivalent type of event will receive
     * the event.
     * @param event The even to post
     * @param <T> The type of event
     */
    <T extends Event> void post(@Nonnull T event);
}
