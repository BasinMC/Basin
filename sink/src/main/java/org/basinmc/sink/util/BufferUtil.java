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
package org.basinmc.sink.util;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.netty.buffer.ByteBuf;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides utility methods which permit the reading and writing of various data and value types
 * from/into a binary format.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public final class BufferUtil {

  /**
   * Defines a default charset which is substituted when none is explicitly given.
   */
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private BufferUtil() {
  }

  /**
   * Checks whether the upcoming bytes within the buffer match the specified magic value and
   * produces an exception upon mismatch.
   *
   * @param buffer an input buffer.
   * @param expected an expected magic value.
   * @param supplier an exception factory.
   * @param <E> an arbitrary exception type.
   * @throws E when the magic value does not match.
   */
  public static <E extends Throwable> void checkMagicValue(@NonNull ByteBuf buffer, int expected,
      @NonNull Supplier<E> supplier) throws E {
    if (expected != buffer.readInt()) {
      throw supplier.get();
    }
  }

  /**
   * <p>Reads an arbitrarily sized payload from the buffer.</p>
   *
   * <p>The resulting buffer will be allocated from the same allocator as the input buffer.</p>
   *
   * @param buffer an input buffer.
   * @return an arbitrarily sized payload or an empty optional (when a length of zero is indicated).
   */
  @NonNull
  public static Optional<ByteBuf> readBuffer(@NonNull ByteBuf buffer) {
    var length = buffer.readUnsignedShort();
    if (length == 0) {
      return Optional.empty();
    }

    var out = buffer.alloc().buffer(length);
    buffer.readBytes(out);

    return Optional.of(out);
  }

  /**
   * Writes an arbitrarily sized payload into the buffer.
   *
   * @param buffer an output buffer.
   * @param value an arbitrarily sized payload.
   */
  public static void writeBuffer(@NonNull ByteBuf buffer, @Nullable ByteBuf value) {
    if (value == null || !value.isReadable()) {
      buffer.writeShort(0);
      return;
    }

    buffer.writeShort(value.readableBytes());
    buffer.writeBytes(value);
  }

  /**
   * Reads an arbitrarily sized payload from the buffer.
   *
   * @param buffer an input buffer.
   * @return an arbitrarily sized payload or an empty optional (when a length of zero is indicated).
   */
  @NonNull
  public static Optional<byte[]> readBytes(@NonNull ByteBuf buffer) {
    var length = buffer.readUnsignedShort();
    if (length == 0) {
      return Optional.empty();
    }

    var out = new byte[length];
    buffer.readBytes(out);

    return Optional.of(out);
  }

  /**
   * Writes an arbitrarily sized payload into the buffer.
   *
   * @param buffer an output buffer.
   * @param value an arbitrarily sized payload.
   */
  public static void writeBytes(@NonNull ByteBuf buffer, @Nullable byte[] value) {
    if (value == null || value.length == 0) {
      buffer.writeShort(0);
      return;
    }

    buffer.writeShort(value.length);
    buffer.writeBytes(value);
  }

  /**
   * Reads a list of an arbitrarily encoded items from the buffer.
   *
   * @param buffer an input buffer.
   * @param collectionFactory a factory which constructs an arbitrary collection implementation.
   * @param itemDecoder a factory which decodes single items from their binary encoding.
   * @param <C> a collection type.
   * @param <I> an item type.
   * @return a collection of items.
   */
  @NonNull
  public static <C extends Collection<I>, I> C readList(@NonNull ByteBuf buffer,
      @NonNull Supplier<C> collectionFactory, @NonNull Function<ByteBuf, I> itemDecoder) {
    var length = buffer.readUnsignedShort();

    var collection = collectionFactory.get();
    for (var i = 0; i < length; ++i) {
      collection.add(itemDecoder.apply(buffer));
    }
    return collection;
  }

  /**
   * Writes a list of arbitrarily encoded items into the buffer.
   *
   * @param buffer an output buffer.
   * @param collection an arbitrary collection of items.
   * @param itemEncoder an encoder which encodes single items into their binary encoding.
   * @param <I> an item type.
   */
  public static <I> void writeList(@NonNull ByteBuf buffer, @NonNull Collection<I> collection,
      @NonNull BiConsumer<ByteBuf, I> itemEncoder) {
    buffer.writeShort(collection.size());
    collection.forEach((item) -> itemEncoder.accept(buffer, item));
  }

  /**
   * @see #readString(ByteBuf, Charset)
   */
  @NonNull
  public static Optional<String> readString(@NonNull ByteBuf buffer) {
    return readString(buffer, DEFAULT_CHARSET);
  }

  /**
   * @see #writeString(ByteBuf, String, Charset)
   */
  public static void writeString(@NonNull ByteBuf buffer, @Nullable String value) {
    writeString(buffer, value, DEFAULT_CHARSET);
  }

  /**
   * Retrieves an encoded string from the specified buffer using a specific charset.
   *
   * @param buffer an input buffer.
   * @param charset a charset.
   * @return an arbitrary string.
   */
  @NonNull
  public static Optional<String> readString(@NonNull ByteBuf buffer, @NonNull Charset charset) {
    return readBytes(buffer)
        .map((bytes) -> new String(bytes, charset));
  }

  /**
   * Writes an arbitrarily sized string into the buffer.
   *
   * @param buffer an output buffer.
   * @param value an arbitrary string.
   * @param charset a charset for encoding purposes.
   */
  public static void writeString(@NonNull ByteBuf buffer, @Nullable String value,
      @NonNull Charset charset) {
    if (value == null || value.isEmpty()) {
      writeBytes(buffer, null);
      return;
    }

    writeBytes(buffer, value.getBytes(charset));
  }

  /**
   * Retrieves an encoded UUID from the buffer.
   *
   * @param buffer an input buffer.
   * @return a UUID or an empty optional if zero length is indicated.
   */
  @NonNull
  public static Optional<UUID> readUUID(@NonNull ByteBuf buffer) {
    return readString(buffer) // FIXME: String encoding sensible for cross platform support?
        .map(UUID::fromString);
  }

  /**
   * Writes an encoded UUID into the buffer.
   *
   * @param buffer an output buffer.
   * @param value a UUID or null.
   */
  public static void writeUUID(@NonNull ByteBuf buffer, @Nullable UUID value) {
    if (value == null) {
      writeString(buffer, null);
      return;
    }

    writeString(buffer, value.toString());
  }
}
