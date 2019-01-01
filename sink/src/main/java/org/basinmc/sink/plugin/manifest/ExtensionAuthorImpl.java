/*
 * Copyright 2018 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.sink.plugin.manifest;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;
import org.basinmc.faucet.extension.manifest.ExtensionAuthor;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class ExtensionAuthorImpl implements ExtensionAuthor {

  private final String name;
  private final String alias;

  public ExtensionAuthorImpl(String name, String alias) {
    this.name = name;
    this.alias = alias;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Optional<String> getAlias() {
    return Optional.ofNullable(this.alias);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExtensionAuthorImpl)) {
      return false;
    }
    ExtensionAuthorImpl that = (ExtensionAuthorImpl) o;
    return Objects.equals(this.name, that.name) &&
        Objects.equals(this.alias, that.alias);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.alias);
  }

  /**
   * Provides a simple de-serializer which permits the representation of extension authors as
   * strings or slightly more complex objects.
   */
  public static class Deserializer implements JsonDeserializer<ExtensionAuthorImpl> {

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ExtensionAuthorImpl deserialize(@NonNull JsonElement json, @NonNull Type typeOfT,
        @NonNull JsonDeserializationContext context) throws JsonParseException {
      if (json.isJsonObject()) {
        var obj = json.getAsJsonObject();

        var nameElement = obj.get("name");
        var aliasElement = obj.get("alias");

        if (nameElement == null) {
          throw new JsonParseException("Author name must be set");
        }

        return new ExtensionAuthorImpl(nameElement.getAsString(),
            aliasElement != null ? aliasElement.getAsString() : null);
      }

      return new ExtensionAuthorImpl(json.getAsString(), null);
    }
  }
}
