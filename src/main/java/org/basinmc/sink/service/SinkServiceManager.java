/*
 * Copyright 2016 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.basinmc.sink.service;

import com.google.common.collect.MapMaker;

import org.basinmc.faucet.observable.AbstractObservableProperty;
import org.basinmc.faucet.service.ServiceManager;
import org.basinmc.faucet.service.ServiceReference;
import org.basinmc.faucet.service.ServiceRegistration;
import org.basinmc.faucet.util.Priority;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * TODO: Leak detection?
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
@ThreadSafe
public class SinkServiceManager implements ServiceManager {
    private final Map<Class<?>, Set> registrationMap; // Using raw types is a bit ugly but Java does not like this sort of thing
    private final Map<SimpleServiceRegistration<?>, Object> proxyMap;
    private final Map<SimpleServiceRegistration<?>, SimpleServiceReference<?>> referenceMap;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public SinkServiceManager() {
        this.registrationMap = (new MapMaker())
                .weakKeys()
                .makeMap();

        this.proxyMap = (new MapMaker())
                .weakKeys()
                .makeMap();

        this.referenceMap = (new MapMaker())
                .weakKeys()
                .weakValues()
                .makeMap();
    }

    /**
     * Creates a new proxy wrapper around an implementation in order to
     *
     * @param interfaceType  an interface type.
     * @param implementation an implementation.
     * @param <I>            an interface type.
     * @return a proxy instance.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <I> I createWeakProxy(@Nonnull Class<I> interfaceType, @Nonnull I implementation) {
        // TODO: This method is great but doesn't cover abstract classes
        // TODO: For now all users are forced to provide interfaces (which is a sane concept anyways)
        if (!interfaceType.isInterface()) {
            throw new IllegalArgumentException("Service base type is required to be an interface");
        }

        return (I) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, new ServiceProxyInvocationHandler<>(implementation));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<ServiceReference<T>> getReference(@Nonnull Class<T> serviceType) {
        this.lock.readLock().lock();

        try {
            return Optional.ofNullable((ServiceReference<T>) this.registrationMap.get(serviceType));
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <I> ServiceRegistration<I> register(@Nonnull Class<I> interfaceType, @Nonnull I implementation, @Nonnull Priority priority) {
        this.lock.writeLock().lock();

        try {
            Set<SimpleServiceRegistration<I>> registrations = this.getRegistrationSet(interfaceType);

            SimpleServiceRegistration<I> registration = new SimpleServiceRegistration<>(interfaceType, priority);

            SimpleServiceRegistration<I> highestPriorityRegistration = registrations.stream().sorted((a, b) -> a.getPriority().compareTo(b.getPriority())).findFirst().orElse(null);

            if (highestPriorityRegistration == registration) {
                for (SimpleServiceRegistration<I> oldRegistration : registrations) {
                    SimpleServiceReference<I> reference = (SimpleServiceReference<I>) this.referenceMap.get(oldRegistration);
                    this.referenceMap.remove(oldRegistration);
                    this.referenceMap.put(registration, reference);
                }
            }

            registrations.add(registration);
            registration.set(implementation);

            return registration;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Retrieves the registration set for a specific interface type.
     *
     * @param interfaceType an interface type.
     * @param <I>           an interface type.
     * @return a set of registrations.
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    private <I> Set<SimpleServiceRegistration<I>> getRegistrationSet(@Nonnull Class<I> interfaceType) {
        if (this.registrationMap.containsKey(interfaceType)) {
            return (Set<SimpleServiceRegistration<I>>) this.registrationMap.get(interfaceType);
        }

        Set<SimpleServiceRegistration<I>> registrations = Collections.newSetFromMap(new WeakHashMap<SimpleServiceRegistration<I>, Boolean>());
        this.registrationMap.put(interfaceType, registrations);
        return registrations;
    }

    /**
     * Pushes an update to all known service references.
     * @param registration a registration.
     * @param value a new implementation.
     * @param <I> an interface type.
     */
    @SuppressWarnings("unchecked")
    private <I> void pushUpdate(@Nonnull SimpleServiceRegistration<I> registration, @Nullable I value) {
        this.lock.writeLock().lock();

        try {
            if (value == null) {
                return;
            }

            final I proxy = createWeakProxy(registration.interfaceType, value);
            this.proxyMap.put(registration, proxy);
            Optional.ofNullable(this.referenceMap.get(registration)).ifPresent((r) -> ((SimpleServiceReference<I>) r).pushUpdate(proxy));
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    /**
     * Provides a simple invocation handler for handling interface proxies.
     *
     * @param <I> a reference type.
     */
    private static class ServiceProxyInvocationHandler<I> implements InvocationHandler {
        private final WeakReference<I> implementation;

        public ServiceProxyInvocationHandler(@Nonnull I implementation) {
            this.implementation = new WeakReference<>(implementation);
        }

        /**
         * {@inheritDoc}
         */
        @Nullable
        @Override
        public Object invoke(@Nonnull Object proxy, @Nonnull Method method, @Nonnull Object[] args) throws Throwable {
            I object = this.implementation.get();

            if (object == null) {
                throw new IllegalStateException("Invalid Proxy Reference - Please fix your code");
            }

            // TODO: This will just let invocation exceptions bubble up ... We might want to switch that out
            method.setAccessible(true);
            return method.invoke(object, args);
        }
    }

    /**
     * Provides a simple service reference.
     *
     * @param <I> a service type.
     */
    @ThreadSafe
    private class SimpleServiceReference<I> extends AbstractObservableProperty<I> implements ServiceReference<I> {
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private I implementation;

        /**
         * {@inheritDoc}
         */
        @Override
        public I get() {
            this.lock.readLock().lock();

            try {
                return this.implementation;
            } finally {
                this.lock.readLock().unlock();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean set(I value) {
            return false;
        }

        /**
         * Pushes a new update into the reference.
         *
         * @param implementation an implementation.
         */
        protected void pushUpdate(@Nonnull I implementation) {
            this.lock.writeLock().lock();

            try {
                this.callObservers(implementation);
                this.implementation = implementation;
            } finally {
                this.lock.writeLock().unlock();
            }
        }
    }

    /**
     * Provides a simple interface for updating and removing service registrations.
     *
     * @param <I> a service type.
     */
    @ThreadSafe
    private class SimpleServiceRegistration<I> extends AbstractObservableProperty<I> implements ServiceRegistration<I> {
        private final Class<I> interfaceType;
        private final Priority priority;
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private I implementation;

        public SimpleServiceRegistration(@Nonnull Class<I> interfaceType, @Nonnull Priority priority) {
            this.interfaceType = interfaceType;
            this.priority = priority;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unregister() {
            SinkServiceManager.this.lock.writeLock().lock();

            try {
                if (this.set(null)) {
                    Set<SimpleServiceRegistration<I>> registrations = SinkServiceManager.this.getRegistrationSet(this.interfaceType);
                    registrations.remove(this);
                    SinkServiceManager.this.proxyMap.remove(this);
                }
            } finally {
                SinkServiceManager.this.lock.writeLock().unlock();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public I get() {
            this.lock.readLock().lock();

            try {
                return this.implementation;
            } finally {
                this.lock.readLock().unlock();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Priority getPriority() {
            return this.priority;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean set(I value) {
            if (value == null) {
                throw new NullPointerException("Service value cannot be null");
            }

            if (!this.callObservers(value)) {
                return false;
            }

            this.lock.writeLock().lock();

            try {
                SinkServiceManager.this.pushUpdate(this, value);
                this.implementation = value;
                return true;
            } finally {
                this.lock.writeLock().unlock();
            }
        }
    }
}
