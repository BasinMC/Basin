/*
 * Copyright 2017 Hex <hex@hex.lc>
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
package org.basinmc.faucet.key;

import java.util.UUID;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * A minecraft key. Has a namespace and a name and is represented in the form {@code
 * namespace:name}.
 */
public interface Key {

  @NonNull
  String getNamespace();

  @NonNull
  String getName();

  @NonNull
  UUID getUUID();
}
