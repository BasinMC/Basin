/*
* Copyright 2016 __0x277F <0x277F@gmail.com>
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
*/

package org.basinmc.faucet;

import javax.annotation.Nonnull;

/**
 * Represents a type that wraps another type. This would primarily be used for API-NMS
 * intermediaries.
 *
 * @param <T> The type being wrapped
 */
public interface Handled<T> {
    @Nonnull
    T getHandle();
}
