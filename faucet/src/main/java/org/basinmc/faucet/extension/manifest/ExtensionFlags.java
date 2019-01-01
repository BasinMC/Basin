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
package org.basinmc.faucet.extension.manifest;

import java.util.Collection;
import org.basinmc.faucet.util.BitMask;

/**
 * Provides a list of valid extension flags which express additional information about a build.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public final class ExtensionFlags extends BitMask<ExtensionFlags> {

  // 1 - 8 are reserved
  public static final ExtensionFlags PRIVATE = new ExtensionFlags(16);
  public static final ExtensionFlags COMMERCIAL = new ExtensionFlags(32);
  // 64 is reserved
  public static final ExtensionFlags CI_BUILD = new ExtensionFlags(128);

  public ExtensionFlags(int mask) {
    super(mask);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ExtensionFlags createInstance(int mask) {
    return new ExtensionFlags(mask);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<ExtensionFlags> values() {
    return null;
  }
}
