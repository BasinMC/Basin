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
package org.basinmc.sink.plugin;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public class ExtensionClassLoader extends URLClassLoader {

  private final ExtensionImpl extension;

  public ExtensionClassLoader(@NonNull ExtensionImpl extension) throws MalformedURLException {
    super(new URL[]{extension.getContainerPath().toUri().toURL()},
        ExtensionClassLoader.class.getClassLoader());
    this.extension = extension;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Class<?> findClass(@NonNull String name) throws ClassNotFoundException {
    try {
      return super.findClass(name);
    } catch (ClassNotFoundException ex) {
      return this.findDependencyClass(name)
          .orElseThrow(() -> ex);
    }
  }

  /**
   * Locates a class within the extension's resolved dependencies.
   *
   * @param name a class name.
   * @return a resolved class or, if none was found, an empty optional.
   */
  @NonNull
  @SuppressWarnings("unchecked")
  private Optional<Class<?>> findDependencyClass(@NonNull String name) {
    return (Optional) this.extension.getResolvedDependencies().stream() // welcome to generic hell
        .flatMap((e) -> e.getClassLoader().stream())
        .flatMap((cl) -> {
          try {
            return Stream.of(cl.findClass(name));
          } catch (ClassNotFoundException ex) {
            return Stream.empty();
          }
        })
        .findFirst();
  }
}
