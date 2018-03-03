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
package org.basinmc.faucet.trace;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.basinmc.faucet.network.NetDirection;
import org.basinmc.faucet.network.NetHandler;
import org.basinmc.faucet.network.Packet;

/**
 * Represents a step in a trace chain in which data is sent or received over the network
 * by way of packets.
 */
public interface TracePacket extends TraceNode {

  /**
   * Gets the type of packet that created this trace node. For example, a
   * {@link NetDirection#SERVERBOUND} value would represent a trace node created when
   * the server receives a packet from the client (and would probably be the beginning of
   * the trace).
   *
   * @return the packet's direction
   */
  @NonNull
  NetDirection getDirection();

  /**
   * Gets the source of the packet that created this trace node. A {@link NetDirection#CLIENTBOUND}
   * value for {@link #getDirection()} will invariably result in this returning an instance
   * of the server's network handler.
   *
   * @return a network handler
   */
  @NonNull
  NetHandler getSource();

  /**
   * Get a snapshot of the packet at the time of processing by the given NetHandler. Any
   * changes made to the packet since processing will not be reflected.
   *
   * @return the packet whose processing triggered this trace node
   */
  @NonNull
  Packet getPacket();
}
