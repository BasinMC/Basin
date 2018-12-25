/*
 * Copyright 2016 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.basinmc.faucet.exception;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Provides a base exception for all issues which arise within a extension (or other bundle) while
 * being called inside of Faucet or one of its implementations.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public abstract class PluginException extends Exception {

  private final Module module;

  public PluginException(@NonNull Module module) {
    super();
    this.module = module;
  }

  public PluginException(@NonNull Module module, String s) {
    super(s);
    this.module = module;
  }

  public PluginException(@NonNull Module module, String s, Throwable throwable) {
    super(s, throwable);
    this.module = module;
  }

  public PluginException(@NonNull Module module, Throwable throwable) {
    super(throwable);
    this.module = module;
  }

  /**
   * Retrieves the offending module.
   */
  @NonNull
  public Module getModule() {
    return this.module;
  }
}
