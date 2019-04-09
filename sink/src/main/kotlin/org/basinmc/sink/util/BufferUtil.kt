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
package org.basinmc.sink.util

import io.netty.buffer.ByteBuf
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier

/**
 * Provides utility methods which permit the reading and writing of various data and value types
 * from/into a binary format.
 *
 * @author [Johannes Donath](mailto:johannesd@torchmind.com)
 */
object BufferUtil {

  /**
   * Defines a default charset which is substituted when none is explicitly given.
   */
  val defaultCharset = StandardCharsets.UTF_8

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
  fun <E : Throwable> checkMagicValue(buffer: ByteBuf, expected: Int,
      supplier: Supplier<E>) {
    if (expected != buffer.readInt()) {
      throw supplier.get()
    }
  }

  /**
   *
   * Reads an arbitrarily sized payload from the buffer.
   *
   *
   * The resulting buffer will be allocated from the same allocator as the input buffer.
   *
   * @param buffer an input buffer.
   * @return an arbitrarily sized payload or an empty optional (when a length of zero is indicated).
   */
  fun readBuffer(buffer: ByteBuf): Optional<ByteBuf> {
    val length = buffer.readUnsignedShort()
    if (length == 0) {
      return Optional.empty()
    }

    val out = buffer.alloc().buffer(length)
    buffer.readBytes(out)

    return Optional.of(out)
  }

  /**
   * Writes an arbitrarily sized payload into the buffer.
   *
   * @param buffer an output buffer.
   * @param value an arbitrarily sized payload.
   */
  fun writeBuffer(buffer: ByteBuf, value: ByteBuf?) {
    if (value == null || !value.isReadable) {
      buffer.writeShort(0)
      return
    }

    buffer.writeShort(value.readableBytes())
    buffer.writeBytes(value)
  }

  /**
   * Reads an arbitrarily sized payload from the buffer.
   *
   * @param buffer an input buffer.
   * @return an arbitrarily sized payload or an empty optional (when a length of zero is indicated).
   */
  fun readBytes(buffer: ByteBuf): Optional<ByteArray> {
    val length = buffer.readUnsignedShort()
    if (length == 0) {
      return Optional.empty()
    }

    val out = ByteArray(length)
    buffer.readBytes(out)

    return Optional.of(out)
  }

  /**
   * Writes an arbitrarily sized payload into the buffer.
   *
   * @param buffer an output buffer.
   * @param value an arbitrarily sized payload.
   */
  fun writeBytes(buffer: ByteBuf, value: ByteArray?) {
    if (value == null || value.isEmpty()) {
      buffer.writeShort(0)
      return
    }

    buffer.writeShort(value.size)
    buffer.writeBytes(value)
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
  fun <C : MutableCollection<I>, I> readList(buffer: ByteBuf,
      collectionFactory: Supplier<C>, itemDecoder: Function<ByteBuf, I>): C {
    val length = buffer.readUnsignedShort()

    val collection = collectionFactory.get()
    for (i in 0 until length) {
      collection.add(itemDecoder.apply(buffer))
    }
    return collection
  }

  /**
   * Writes a list of arbitrarily encoded items into the buffer.
   *
   * @param buffer an output buffer.
   * @param collection an arbitrary collection of items.
   * @param itemEncoder an encoder which encodes single items into their binary encoding.
   * @param <I> an item type.
  </I> */
  fun <I> writeList(buffer: ByteBuf, collection: Collection<I>,
      itemEncoder: BiConsumer<ByteBuf, I>) {
    buffer.writeShort(collection.size)
    collection.forEach { item -> itemEncoder.accept(buffer, item) }
  }

  /**
   * Retrieves an encoded string from the specified buffer using a specific charset.
   *
   * @param buffer an input buffer.
   * @param charset a charset.
   * @return an arbitrary string.
   */
  @JvmOverloads
  fun readString(buffer: ByteBuf, charset: Charset = defaultCharset): Optional<String> {
    return readBytes(buffer)
        .map { bytes -> String(bytes, charset) }
  }

  /**
   * Writes an arbitrarily sized string into the buffer.
   *
   * @param buffer an output buffer.
   * @param value an arbitrary string.
   * @param charset a charset for encoding purposes.
   */
  @JvmOverloads
  fun writeString(buffer: ByteBuf, value: String?,
      charset: Charset = defaultCharset) {
    if (value == null || value.isEmpty()) {
      writeBytes(buffer, null)
      return
    }

    writeBytes(buffer, value.toByteArray(charset))
  }

  /**
   * Retrieves an encoded UUID from the buffer.
   *
   * @param buffer an input buffer.
   * @return a UUID or an empty optional if zero length is indicated.
   */
  fun readUUID(buffer: ByteBuf): Optional<UUID> {
    return readString(buffer) // FIXME: String encoding sensible for cross platform support?
        .map { UUID.fromString(it) }
  }

  /**
   * Writes an encoded UUID into the buffer.
   *
   * @param buffer an output buffer.
   * @param value a UUID or null.
   */
  fun writeUUID(buffer: ByteBuf, value: UUID?) {
    if (value == null) {
      writeString(buffer, null)
      return
    }

    writeString(buffer, value.toString())
  }
}
