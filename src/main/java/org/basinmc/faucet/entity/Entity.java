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
package org.basinmc.faucet.entity;

import org.basinmc.faucet.CapabilityHolder;
import org.basinmc.faucet.math.Vector3;
import org.basinmc.faucet.world.WorldObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base interface for all entities in the game.
 */
public interface Entity extends CapabilityHolder, WorldObject<Entity> {
    /**
     * Gets the internal entity ID of the entity. This value should not be used
     * by API methods.
     */
    int getId();

    /**
     * Gets the entity this entity is riding, or null if the entity is not
     * riding a vehicle.
     *
     * @return another entity
     */
    @Nullable
    Entity getVehicle();

    /**
     * Gets the current motion values for this entity.
     * TODO Do we really want to store motion in an immutable vector? Let's figure this out later.
     *
     * @return a motion vector
     */
    @Nonnull
    Vector3 getMotion();

    /**
     * Check if the entity is dead. This does not have anything to do with the entity existing
     * on the server.
     */
    boolean isDead();

    /**
     * Check if the entity is valid. This means that the entity, alive or dead, exists somewhere
     * on the server.
     */
    boolean isValid();
}
