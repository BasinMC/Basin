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
package org.basinmc.faucet.extension.dependency;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Objects;

/**
 * Provides a reference to a service implementation.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public class ServiceReference {

  private final String baseClassName;
  private final String version;

  public ServiceReference(@NonNull String baseClassName, @NonNull String version) {
    this.baseClassName = baseClassName;
    this.version = version;
  }

  @NonNull
  public String getBaseClassName() {
    return this.baseClassName;
  }

  @NonNull
  public String getVersion() {
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
    if (!(o instanceof ServiceReference)) {
      return false;
    }
    ServiceReference that = (ServiceReference) o;
    return Objects.equals(this.baseClassName, that.baseClassName) &&
        Objects.equals(this.version, that.version);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.baseClassName, this.version);
  }
}
