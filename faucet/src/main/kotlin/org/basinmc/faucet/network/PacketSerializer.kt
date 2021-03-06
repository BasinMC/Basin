/*
 * Copyright 2016 Hex <hex@hex.lc>
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
 *
 */
package org.basinmc.faucet.network

/**
 * Converts between API objects and internal packet data types.
 *
 * @param <T> the packet type
 */
interface PacketSerializer<T : Packet> {

  /**
   * Parse the given packet's fields, storing its fields in order. If this serializer
   * already contains changes to fields at the time of calling this method, then those
   * changes will be applied to the packet. Afterwards, the packet with its mutated values
   * will be read into the serializer.
   *
   * @param packet the packet to parse
   */
  fun accept(packet: T)

  /**
   * Retrieve the value of the field at the given index.
   *
   * @param fieldIndex the index to retrieve from
   * @param <F> the type of the field at the given index
   * @return the field's value, or null if none is found
   */
  fun <F> read(fieldIndex: Int): F?

  /**
   * Write the given value to the field at the given index
   *
   * @param fieldIndex the index to write at
   * @param value the value to set the field to
   * @param <F> the type of the field at the given index
   */
  fun <F> write(fieldIndex: Int, value: F?)
}
