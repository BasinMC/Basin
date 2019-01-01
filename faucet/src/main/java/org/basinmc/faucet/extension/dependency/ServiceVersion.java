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
package org.basinmc.faucet.extension.dependency;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Objects;
import org.basinmc.faucet.util.Version;

/**
 * References a single service version.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public class ServiceVersion {

  private final String identifier;
  private final Version version;

  public ServiceVersion(@NonNull String identifier, @NonNull Version version) {
    this.identifier = identifier;
    this.version = version;
  }

  @NonNull
  public String getIdentifier() {
    return this.identifier;
  }

  @NonNull
  public Version getVersion() {
    return this.version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ServiceVersion)) {
      return false;
    }
    ServiceVersion that = (ServiceVersion) o;
    return Objects.equals(this.identifier, that.identifier) &&
        Objects.equals(this.version, that.version);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.identifier, this.version);
  }
}
