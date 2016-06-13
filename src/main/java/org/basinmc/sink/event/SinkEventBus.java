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
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class SinkEventBus implements EventBus {
    private ReadWriteLock lock;
    private Map<Class<? extends Event>, CopyOnWriteArraySet<EventHandler>> handlers;
    private Map<Class<?>, EventHandler[]> handlerHolders;
    private AsmWrapperFactory<EventHandler, Event> wrapperFactory;

    public SinkEventBus() {
        this.lock = new ReentrantReadWriteLock();
        this.handlers = new ConcurrentHashMap<>();
        this.handlerHolders = new ConcurrentHashMap<>();
        this.wrapperFactory = new AsmWrapperFactory<>(EventSubscribe.class, EventHandler.class);
    }

    @Override
    public <T extends Event> boolean subscribe(@Nonnull EventHandler<T> handler, @Nonnull Class<T>... eventType) {
        lock.writeLock().lock();
        lock.readLock().lock();
        boolean added = false;
        for (Class<T> clazz : eventType) {
            CopyOnWriteArraySet<EventHandler> handlerList;
            if (!handlers.containsKey(clazz)) {
                handlerList = new CopyOnWriteArraySet<>();
            } else {
                handlerList = handlers.get(clazz);
            }
            if (handlerList.contains(handler)) {
                continue;
            }
            handlerList.add(handler);
            handlers.put(clazz, handlerList);
            added = true;
        }
        lock.writeLock().unlock();
        lock.readLock().unlock();
        return added;
    }

    @Override
    public <T extends Event> boolean unsubscribe(@Nullable EventHandler<T> handler) {
        lock.readLock().lock();
        lock.writeLock().lock();
        final boolean[] removed = {false}; // Whatever. I'll do something better later. Maybe.
        handlers.forEach((type, handlerList) -> {
            if (handlerList.contains(handler)) {
                handlerList.remove(handler);
                removed[0] = true;
            }
        });
        lock.readLock().unlock();
        lock.writeLock().unlock();
        return removed[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> Collection<EventHandler<? extends T>> unsubscribeAll(@Nonnull Class<T> eventType) {
        Collection<EventHandler<? extends T>> removed = new HashSet<>();
        lock.readLock().lock();
        lock.writeLock().lock();
        handlers.get(eventType).forEach(removed::add);
        handlers.get(eventType).removeAll(removed);
        return removed;
    }

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

    @Override
    public boolean subscribe(@Nonnull Class<?> type) {
        // TODO Not sure exactly how I'm gonna implement this.
        return false;
    }

    @Override
    public boolean unsubscribe(@Nullable Object holder) {
        lock.readLock().lock();
        if (holder == null) return true;
        Class<?> clazz = holder.getClass();
        handlers.values().stream().forEach(list -> list.stream()
                .filter(handler -> handler.getClass().getDeclaredFields()[0].getType().equals(clazz))
                .forEach(handler -> handlers.values().forEach(set -> set.remove(handler))));
        return true; // TODO figure this one out
    }

    @Override
    public boolean unsubscribe(@Nullable Class<?> type) {
        return false;
    }

    @Override
    public boolean subscribe(@Nonnull Method method) {
        if (!method.isAnnotationPresent(EventSubscribe.class)) return false;
        Class<? extends EventHandler> wrapper = wrapperFactory.createWrapper(method);
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

    @Override
    public <T extends Event> boolean isRegistered(@Nonnull EventHandler<T> handler) {
        lock.readLock().lock();
        boolean r = handlers.values().stream().anyMatch(list -> list.contains(handler));
        lock.readLock().unlock();
        return r;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> Collection<EventHandler<? super T>> getHandlers(@Nullable Class<T> eventType) {
        lock.readLock().lock();
        Collection<EventHandler<? super T>> handlerList = new HashSet<>();
        if (eventType == null) {
            return handlerList;
        }
        handlers.keySet().stream().filter(eventType::isAssignableFrom).forEach(clazz -> handlerList.add((EventHandler<? super T>) handlers.get(clazz)));
        return handlerList;
    }
}
