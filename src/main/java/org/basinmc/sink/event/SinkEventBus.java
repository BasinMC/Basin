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
package org.basinmc.sink.event;

import org.basinmc.faucet.event.Event;
import org.basinmc.faucet.event.EventBus;
import org.basinmc.faucet.event.EventHandler;
import org.basinmc.faucet.event.EventSubscribe;
import org.basinmc.sink.util.AsmWrapperFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class SinkEventBus implements EventBus {
    private Map<Class<? extends Event>, CopyOnWriteArraySet<EventHandler>> handlers;
    private Map<Class<?>, EventHandler[]> handlerHolders;
    private AsmWrapperFactory<EventHandler, Event> wrapperFactory;

    public SinkEventBus() {
        this.handlers = new ConcurrentHashMap<>();
        this.handlerHolders = new ConcurrentHashMap<>();
        this.wrapperFactory = new AsmWrapperFactory<>(EventSubscribe.class, EventHandler.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Event> boolean subscribe(@Nonnull EventHandler<T> handler, @Nonnull Class<T>... eventType) {
        boolean added = false;

        for (Class<T> clazz : eventType) {
            CopyOnWriteArraySet<EventHandler> handlerList;

            if (!this.handlers.containsKey(clazz)) {
                handlerList = new CopyOnWriteArraySet<>();
            } else {
                handlerList = this.handlers.get(clazz);
            }

            if (handlerList.contains(handler)) {
                continue;
            }

            handlerList.add(handler);
            this.handlers.put(clazz, handlerList);
            added = true;
        }
        return added;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Event> boolean unsubscribe(@Nullable EventHandler<T> handler) {
        final boolean[] removed = {false}; // Whatever. I'll do something better later. Maybe.

        this.handlers.forEach((type, handlerList) -> {
            if (handlerList.contains(handler)) {
                handlerList.remove(handler);
                removed[0] = true;
            }
        });

        return removed[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> Collection<EventHandler<? extends T>> unsubscribeAll(@Nonnull Class<T> eventType) {
        Collection<EventHandler<? extends T>> removed = new HashSet<>();

        this.handlers.get(eventType).forEach(removed::add);
        this.handlers.get(eventType).removeAll(removed);

        return removed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean subscribe(@Nonnull Object holder) {
        Class clazz = holder.getClass();
        if (!clazz.isAnnotationPresent(EventSubscribe.class)) {
            return false;
        }
        boolean r = true;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventSubscribe.class)) {
                if (!subscribe(method)) {
                    r = false;
                }
            }
        }
        return r;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean subscribe(@Nonnull Class<?> type) {
        // TODO Not sure exactly how I'm gonna implement this.
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unsubscribe(@Nullable Object holder) {
        if (holder == null) return true;

        Class<?> clazz = holder.getClass();

        this.handlers.values().stream().forEach(list -> list.stream()
                .filter(handler -> handler.getClass().getDeclaredFields()[0].getType().equals(clazz))
                .forEach(handler -> this.handlers.values().forEach(set -> set.remove(handler))));
        return true; // TODO figure this one out
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unsubscribe(@Nullable Class<?> type) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean subscribe(@Nonnull Method method) {
        if (!method.isAnnotationPresent(EventSubscribe.class)) return false;
        Class<? extends EventHandler> wrapper = this.wrapperFactory.createWrapper(method);
        List<Class<? extends Event>> types = new ArrayList<>();
        types.addAll(Arrays.asList(method.getAnnotation(EventSubscribe.class).value()));
        types.stream().filter(clazz -> clazz.isAssignableFrom(method.getParameterTypes()[0])).forEach(types::remove);
        try {
            EventHandler handler = wrapper.newInstance();
            subscribe(handler, (Class[]) types.toArray());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Event> boolean isRegistered(@Nonnull EventHandler<T> handler) {
        return this.handlers.values().stream().anyMatch(list -> list.contains(handler));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> Collection<EventHandler<? super T>> getHandlers(@Nullable Class<T> eventType) {
        Collection<EventHandler<? super T>> handlerList = new HashSet<>();

        if (eventType == null) {
            return handlerList;
        }

        this.handlers.keySet().stream().filter(eventType::isAssignableFrom).forEach(clazz -> handlerList.add((EventHandler<? super T>) this.handlers.get(clazz)));
        return handlerList;
    }
}
