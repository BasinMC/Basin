/*
 * Copyright 2019 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.sink.plugin;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.basinmc.faucet.extension.Extension;
import org.basinmc.faucet.extension.ExtensionHelper;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
@Service
public class ExtensionHelperImpl implements ExtensionHelper {

  private final StackWalker stackWalker = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Optional<ExtensionImpl> getCallingExtension() {
    var blacklistedClass = this.stackWalker.getCallerClass();

    return this.stackWalker
        .walk(stream -> mapToExtension(stream, Set.of(blacklistedClass)).findFirst());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Optional<Extension> getFirstCallingExtension() {
    var blacklistedClass = this.stackWalker.getCallerClass();

    return this.stackWalker.walk(stream -> {
      var queue = new ArrayDeque<ExtensionImpl>();
      mapToExtension(stream, Set.of(blacklistedClass)).forEach(queue::push);

      return Optional.ofNullable(queue.pollFirst());
    });
  }

  @NonNull
  private static Stream<ExtensionImpl> mapToExtension(@NonNull Stream<StackFrame> stream,
      @NonNull Set<Class<?>> blacklistedClasses) {
    return stream
        .map(StackFrame::getDeclaringClass)
        .filter(blacklistedClasses::contains)
        .filter(clazz -> clazz.getClassLoader() instanceof ExtensionClassLoader)
        .map(clazz -> ((ExtensionClassLoader) clazz.getClassLoader()).getExtension());
  }
}
