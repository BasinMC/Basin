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
import org.basinmc.faucet.util.VersionRange;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public class ServiceDependency extends ServiceReference {

  private final boolean optional;

  public ServiceDependency(@NonNull String baseClassName, @NonNull VersionRange versionRange,
      boolean optional) {
    super(baseClassName, versionRange);
    this.optional = optional;
  }

  public boolean isOptional() {
    return this.optional;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ServiceDependency)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ServiceDependency that = (ServiceDependency) o;
    return this.optional == that.optional;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.optional);
  }
}
