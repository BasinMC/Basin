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
package org.basinmc.faucet.util;

/**
 * Provides a boolean state which indicates whether an action is to be allowed or prevented. In
 * addition, a default value or behavior may be indicated to the implementing party in order to
 * further interact with the backing logic in a forward compatible manner.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public enum State {

  /**
   * Refers to an allowed action (e.g. an action that will be or already has been carried out by
   * the respective implementation).
   */
  ALLOW,

  /**
   * Refers to a denied action (e.g. an action that won't be or hasn't been carried out by the
   * respective implementation).
   */
  DENY,

  /**
   * Refers to the implementation default state.
   *
   * This special value may be used to refer to or reset to a default value within the respective
   * implementation in a forwards compatible manner.
   */
  DEFAULT,

  /**
   * Refers to any state (including the default state if such state is considered part of the
   * possible values within the respective implementation).
   *
   * This state shall only be used for filters in order to refer to all possible values within the
   * context of the implementation.
   */
  WILDCARD
}
