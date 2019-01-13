/*
 * Copyright 2018 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.sink.plugin.manifest;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Objects;
import java.util.Optional;
import org.basinmc.chloramine.manifest.metadata.Author;
import org.basinmc.faucet.extension.manifest.ExtensionAuthor;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class ExtensionAuthorImpl implements ExtensionAuthor {

  private final Author source;

  public ExtensionAuthorImpl(@NonNull Author source) {
    this.source = source;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public String getName() {
    return this.source.getName();
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Optional<String> getAlias() {
    return this.source.getAlias();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExtensionAuthorImpl)) {
      return false;
    }
    ExtensionAuthorImpl that = (ExtensionAuthorImpl) o;
    return Objects.equals(this.source, that.source);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.source);
  }
}
