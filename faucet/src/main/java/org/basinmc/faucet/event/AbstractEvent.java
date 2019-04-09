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
package org.basinmc.faucet.event;

import edu.umd.cs.findbugs.annotations.NonNull;
import javax.annotation.Nullable;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class AbstractEvent<S> implements Event<S> {

  protected S state;
  protected final S defaultState;

  public AbstractEvent(S state) {
    this.state = state;
    this.defaultState = state;
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public S getDefaultState() {
    return this.defaultState;
  }

  protected void setState(@NonNull S state) {
    this.state = state;
  }
}