/*
 * Copyright 2016 Hex <hex@hex.lc>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License&quot￼;
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
 *
 */
package org.basinmc.faucet.trace;

import javax.annotation.Nonnull;
import org.osgi.framework.Bundle;

/**
 * Represents a call to a specific Faucet API method that is used in a trace chain. This
 * distinction is to allow for easily recognizing a trace chain that starts with an API call
 * and is therefore the result of a plugin.
 */
public interface TraceAPICall extends TraceMethod {

  /**
   * Get the bundle which performed the API call. In <strong>most</strong> cases this will
   * be a plugin bundle, but some circumstances may occur where it is not.
   *
   * @return an OSGi bundle
   */
  @Nonnull
  Bundle getBundle();
}
