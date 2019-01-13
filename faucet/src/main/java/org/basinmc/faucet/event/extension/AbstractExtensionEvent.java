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
import org.basinmc.faucet.extension.Extension;

/**
 * Provides an abstract extension event implementation which implements the bare minimum required to
 * represent the event contents.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class AbstractExtensionEvent<S> implements ExtensionEvent<S> {

  private final Extension extension;

  public AbstractExtensionEvent(Extension extension) {
    this.extension = extension;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Extension getExtension() {
    return this.extension;
  }
}
