/*
 * Copyright 2019 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.sink.util

import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import org.springframework.context.SmartLifecycle

/**
 * Provides a simplified smart lifecycle implementation which handles the internal state of the
 * component (e.g. running, stopped).
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 * @since 1.0
 */
abstract class LifecycleService : SmartLifecycle {

  private val lock = ReentrantReadWriteLock()
  private var running: Boolean = false

  /**
   * {@inheritDoc}
   */
  override fun start() {
    this.lock.writeLock().lock()
    try {
      if (this.running) {
        return
      }

      this.onStart()
    } finally {
      this.lock.writeLock().unlock()
    }
  }

  /**
   * {@inheritDoc}
   */
  override fun stop() {
    this.lock.writeLock().lock()
    try {
      if (!this.running) {
        return
      }

      this.onStop()
    } finally {
      this.lock.writeLock().unlock()
    }
  }

  /**
   * {@inheritDoc}
   */
  override fun isRunning(): Boolean {
    this.lock.readLock().lock()
    try {
      return this.running
    } finally {
      this.lock.readLock().unlock()
    }
  }

  /**
   *
   * Performs the service startup.
   *
   *
   * Implementations of this method must call their super method at the very end of their method
   * body.
   */
  protected open fun onStart() {
    this.running = true
  }

  /**
   *
   * Performs the service shutdown.
   *
   *
   * Implementations of this method must call their super method at the very end of their method
   * body.
   */
  protected open fun onStop() {
    this.running = false
  }
}
