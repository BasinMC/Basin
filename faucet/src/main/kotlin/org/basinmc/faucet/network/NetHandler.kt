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
 * Base interface for objects that can send and receive packets.
 */
interface NetHandler {

  /**
   * Get the network handler's active protocol version. Versions can be found at
   * [wiki.vg](http://wiki.vg/Protocol_History#1.11).
   *
   * @return the protocol version as a positive integer
   */
  val protocolVersion: Int

  /**
   * Determine which protocol versions this is capable of communicating with. Versions can
   * be found at [wiki.vg](http://wiki.vg/Protocol_History#1.11).
   *
   * @return an array of protocol versions
   */
  val protocolCompatibility: IntArray

  /**
   * Send a packet to this network handler.
   *
   * @param packet the packet to send
   */
  fun sendPacket(packet: Packet)
}
