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
package org.basinmc.faucet.event.extension;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nonnull;
import org.basinmc.faucet.extension.Extension;
import org.basinmc.faucet.extension.Extension.Phase;
import org.basinmc.faucet.util.BitMask;

/**
 * Notifies listeners about an in-progress event load.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface ExtensionLoadEvent<S> extends ExtensionPhaseEvent<S> {

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  default Phase getCurrentPhase() {
    return Phase.RESOLVED;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  default Phase getTargetPhase() {
    return Phase.LOADED;
  }

  final class Pre extends AbstractStatefulExtensionEvent<State> implements
      ExtensionLoadEvent<State> {

    public Pre(@NonNull Extension extension) {
      this(extension, State.LOAD);
    }

    public Pre(@NonNull Extension extension, @NonNull State state) {
      super(extension, state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(@NonNull State state) {
      super.setState(state);
    }
  }

  final class Post extends AbstractExtensionEvent<Void> implements ExtensionLoadEvent<Void> {

    public Post(@NonNull Extension extension) {
      super(extension);
    }


  }

  final class State extends BitMask<State> {

    public static final State LOAD = new State(0b0000_0000_0000_0001);

    public State(int mask) {
      super(mask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected State createInstance(int mask) {
      return new State(mask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<State> values() {
      return Set.of(LOAD);
    }
  }
}