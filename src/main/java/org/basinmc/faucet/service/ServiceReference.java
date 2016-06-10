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
package org.basinmc.faucet.service;

import org.basinmc.faucet.observable.ObservableProperty;

/**
 * Represents a registered service.
 *
 * <strong>Note:</strong> Do not permanently store any of the values returned by this interface.
 * These values might become invalidated in the future due to plugin unloading and thus will be
 * replaced through this reference. If you require initialization to occur on a service, please do
 * so by using an observer.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface ServiceReference<T> extends ObservableProperty<T> {
}
