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
import org.basinmc.faucet.util.Priority;
import org.basinmc.sink.util.AsmWrapperFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class SinkEventBus implements EventBus {
    private Map<Class<? extends Event>, CopyOnWriteArrayList<EventHandler<? extends Event>>> handlers;
    private AsmWrapperFactory<EventHandler, Event> wrapperFactory;

    public SinkEventBus() {
        this.handlers = new ConcurrentHashMap<>();
        this.wrapperFactory = new AsmWrapperFactory<>(EventSubscribe.class, EventHandler.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> boolean subscribe(@Nonnull EventHandler<T> handler, @Nonnull Class<? extends Event>[] eventType) {
        boolean added = false;

        for (Class<? extends Event> clazz : eventType) {
            CopyOnWriteArrayList<EventHandler<? extends Event>> handlerList;

            if (!this.handlers.containsKey(clazz)) {
                handlerList = new CopyOnWriteArrayList<>();
            } else {
                handlerList = this.handlers.get(clazz);
            }

            if (handlerList.contains(handler)) {
                continue;
            }

            // Should always be present.
            if (!handler.getClass().isAnnotationPresent(EventSubscribe.class)) {
                throw new IllegalArgumentException("Event handler " + handler.getClass().getName() + " has no @EventSubscribe annotation.");
            }
            EventSubscribe annotation = handler.getClass().getAnnotation(EventSubscribe.class);
            Priority priority = annotation.priority();
            int insertIndex = 0;
            for (int i = 0; i < handlerList.size(); i++) {
                EventHandler h = handlerList.get(i);
                Priority priority1 = h.getClass().getAnnotation(EventSubscribe.class).priority();
                if (priority.ordinal() < priority1.ordinal()) {
                    insertIndex = i - 1;
                    break;
                }
            }

            handlerList.add(insertIndex, handler);
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
        return this.handlers.values().stream()
                .filter((l) -> {
                    l.remove(handler);
                    return l.contains(handler);
                })
                .findAny().isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> Collection<EventHandler<? extends T>> unsubscribeAll(@Nonnull Class<T> eventType) {
        Collection<EventHandler<? extends T>> removed = new HashSet<>();

        this.handlers.get(eventType).forEach(handler -> removed.add((EventHandler<? extends T>) handler));
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
                if (!subscribe(holder, method)) {
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
    @SuppressWarnings("unchecked")
    public <T extends Event> boolean subscribe(@Nonnull Object holder, @Nonnull Method method) {
        if (!method.isAnnotationPresent(EventSubscribe.class)) return false;
        Class<? extends EventHandler> wrapper = this.wrapperFactory.createWrapper(method);
        List<Class<? extends Event>> types = new ArrayList<>();
        types.addAll(Arrays.asList(method.getAnnotation(EventSubscribe.class).value()));
        Collection<Class<? extends Event>> toRemove = types.stream().filter(clazz -> clazz.isAssignableFrom(method.getParameterTypes()[0])).collect(Collectors.toSet());
        types.removeAll(toRemove);
        try {
            EventHandler<T> handler = wrapper.getDeclaredConstructor(method.getDeclaringClass()).newInstance(holder);
            Class<? extends Event>[] typeArray = new Class[types.size()];
            types.toArray(typeArray);
            subscribe(handler, typeArray);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
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

        this.handlers.keySet().stream().filter(type -> type.isAssignableFrom(eventType))
                .forEach(clazz -> handlerList.addAll((Collection<? extends EventHandler<? super T>>) this.handlers.get(clazz)));
        return handlerList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void post(@Nonnull T event) {
        Class<T> eventType = (Class<T>) event.getClass();
        handlers.keySet().stream().filter(clazz -> clazz.isAssignableFrom(eventType))
                .forEachOrdered(clazz -> handlers.get(clazz)
                .forEach(handler -> handler.handle(event)));
    }
}
