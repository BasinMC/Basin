/*
 * Copyright 2018 Johannes Donath  <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package org.basinmc.sink.util;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.context.SmartLifecycle;

/**
 * Provides a simplified smart lifecycle implementation which handles the internal state of the
 * component (e.g. running, stopped).
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public abstract class LifecycleService implements SmartLifecycle {

  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private boolean running;

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    this.lock.writeLock().lock();
    try {
      if (this.running) {
        return;
      }

      this.onStart();
    } finally {
      this.lock.writeLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() {
    this.lock.writeLock().lock();
    try {
      if (!this.running) {
        return;
      }

      this.onStop();
    } finally {
      this.lock.writeLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRunning() {
    this.lock.readLock().lock();
    try {
      return this.running;
    } finally {
      this.lock.readLock().unlock();
    }
  }

  /**
   * <p>Performs the service startup.</p>
   *
   * <p>Implementations of this method must call their super method at the very end of their method
   * body.</p>
   */
  protected void onStart() {
    this.running = true;
  }

  /**
   * <p>Performs the service shutdown.</p>
   *
   * <p>Implementations of this method must call their super method at the very end of their method
   * body.</p>
   */
  protected void onStop() {
    this.running = false;
  }
}
