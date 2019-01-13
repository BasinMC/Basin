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
import javax.annotation.Nonnull;
import org.basinmc.faucet.extension.Extension;
import org.basinmc.faucet.extension.Extension.Phase;

/**
 * Notifies listeners about the removal of an extension in an arbitrary phase.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface ExtensionRemovalEvent extends ExtensionPhaseEvent<Void> {

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  default Phase getCurrentPhase() {
    return this.getExtension().getPhase();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  default Phase getTargetPhase() {
    return Phase.NONE;
  }

  final class Pre extends AbstractExtensionEvent<Void> implements ExtensionRemovalEvent {

    public Pre(@NonNull Extension extension) {
      super(extension);
    }
  }

  final class Post extends AbstractExtensionEvent<Void> implements ExtensionRemovalEvent {

    public Post(@NonNull Extension extension) {
      super(extension);
    }
  }
}
