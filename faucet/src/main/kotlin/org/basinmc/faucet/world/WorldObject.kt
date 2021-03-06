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
package org.basinmc.faucet.world


import org.basinmc.faucet.math.*

/**
 * Represents something with an absolute position in the world.
 */
interface WorldObject { // TODO: Differentiate between entities and objects?

  /**
   * Get an immutable copy of this object's position in the world at this moment.
   *
   * @return a world position
   */
  val position: WorldVectorDouble

  /**
   * Move the object within the same world. In implementations for axis-aligned objects
   * (e.g. [Block]) the vector should be floored before the transformation occurs.
   * If the implementation is mutable, then this will return the same object; if it is not,
   * this will return a new object to refer to the object at its new location.
   *
   * @param vector the translation vector
   * @return an instance of the new object, or the same object if no change occured.
   */
  fun translate(vector: Vector3Double): WorldObject

  /**
   * Checks for an object in the world of the *same type* as this and returns it if it
   * exists. While this would likely be useless for entities, axis-aligned objects such as
   * blocks can be easily compared with this method.
   *
   * @param direction the direction relative to this object from which to search
   * @return an existing world object of same type, or null if none is found
   */
  fun getOffset(direction: Direction): WorldObject?
}
