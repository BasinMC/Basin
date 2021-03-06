/*
 *  Copyright 2016 Hex <hex@hex.lc>
 *  and other copyright owners as documented in the project's IP log.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License&quot￼;
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.basinmc.faucet.world

import org.basinmc.faucet.entity.Entity
import org.basinmc.faucet.math.Vector3
import org.basinmc.faucet.math.Vector3Double
import java.util.*

interface World {

  /**
   * Retrieve the world's associated properties object. Changes to this object should
   * be reflected in the world.
   *
   * @return a properties object
   */
  val properties: WorldProperties

  /**
   * Gets a set of entities in this world, even if they're dead.
   */
  val entities: Set<Entity>

  /**
   * Gets the Random object used by the internal Minecraft world.
   */
  val random: Random

  /**
   * Searches the given spherical area for entities.
   *
   * @param location the center of the search sphere
   * @param radius the radius of the search sphere
   * @return a set of entities within the sphere
   */
  // TODO: get operator?
  fun getEntitiesAt(location: Vector3Double, radius: Double): Set<Entity>


}
