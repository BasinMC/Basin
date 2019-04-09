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

import org.basinmc.faucet.math.BlockAlignedVector3
import org.basinmc.faucet.math.Vector2I

/**
 * Represents various properties about a world. Default values are taken from
 * net.minecraft.world.storage.WorldInfo
 */
class WorldProperties(private var spawn: BlockAlignedVector3?,
    @Deprecated("NMS Magic Number")
    val dimensionId: Int, // TODO: Deprecated NMS magic number - Really necessary?
    private var name: String?) {

  // FIXME: Not fully migrated yet
  //        Do we really need to make this mutable? Optimally worldgen parameters should be final
  private var seaLevel = 63
  private var difficulty = Difficulty.NORMAL
  val timer = Timer()
  private var hardcore = false
  private var defaultGamemode = GameMode.SURVIVAL
  private var borderEnabled = false
  private var worldBorder = Border()
  private var rainTime = 0

  fun getSeaLevel(): Int {
    return seaLevel
  }

  fun setSeaLevel(seaLevel: Int): WorldProperties {
    this.seaLevel = seaLevel
    return this
  }

  fun getDifficulty(): Difficulty {
    return difficulty
  }

  fun setDifficulty(difficulty: Difficulty): WorldProperties {
    this.difficulty = difficulty
    return this
  }

  fun getSpawn(): BlockAlignedVector3? {
    return spawn
  }

  fun setSpawn(spawn: BlockAlignedVector3): WorldProperties {
    this.spawn = spawn
    return this
  }

  fun getName(): String? {
    return name
  }

  fun setName(name: String): WorldProperties {
    this.name = name
    return this
  }

  fun isHardcore(): Boolean {
    return hardcore
  }

  fun setHardcore(hardcore: Boolean): WorldProperties {
    this.hardcore = hardcore
    return this
  }

  fun getDefaultGamemode(): GameMode {
    return defaultGamemode
  }

  fun setDefaultGamemode(defaultGamemode: GameMode): WorldProperties {
    this.defaultGamemode = defaultGamemode
    return this
  }

  fun isBorderEnabled(): Boolean {
    return borderEnabled
  }

  fun setBorderEnabled(borderEnabled: Boolean): WorldProperties {
    this.borderEnabled = borderEnabled
    return this
  }

  fun getWorldBorder(): Border {
    return worldBorder
  }

  fun setWorldBorder(worldBorder: Border): WorldProperties {
    this.worldBorder = worldBorder
    return this
  }

  fun getRainTime(): Int {
    return rainTime
  }

  fun setRainTime(rainTime: Int): WorldProperties {
    this.rainTime = rainTime
    return this
  }

  /**
   * Properties relating to a world border. Default values are taken from
   * net.minecraft.world.storage.WorldInfo
   */
  class Border {

    private var center = Vector2I(0, 0)
    private var radius = 6.0E7
    private var safeZone = 5.0
    private var damage = 0.2
    private var warningDistance = 5
    private var warningTime = 15

    /**
     * Get the block coordinates of the center of the world border
     *
     * @return a 2 dimensional integer vector
     */
    fun getCenter(): Vector2I {
      return center
    }

    fun setCenter(center: Vector2I): Border {
      this.center = center
      return this
    }

    /**
     * Get the distance which the world border extends in each direction from the center.
     *
     * @return distance, in blocks
     */
    fun getRadius(): Double {
      return radius
    }

    fun setRadius(radius: Double): Border {
      this.radius = radius
      return this
    }

    fun getSafeZone(): Double {
      return safeZone
    }

    fun setSafeZone(safeZone: Double): Border {
      this.safeZone = safeZone
      return this
    }

    /**
     * Get the amount of damage dealt per block to players who cross the border
     */
    fun getDamage(): Double {
      return damage
    }

    fun setDamage(damage: Double): Border {
      this.damage = damage
      return this
    }

    fun getWarningDistance(): Int {
      return warningDistance
    }

    fun setWarningDistance(warningDistance: Int): Border {
      this.warningDistance = warningDistance
      return this
    }

    fun getWarningTime(): Int {
      return warningTime
    }

    fun setWarningTime(warningTime: Int): Border {
      this.warningTime = warningTime
      return this
    }
  }


  class Timer {

    private var totalTime = 0
    private var localTime = 0

    fun getLocalTime(): Int {
      return localTime
    }

    fun setLocalTime(localTime: Int): Timer {
      this.localTime = localTime
      return this
    }

    fun getTotalTime(): Int {
      return totalTime
    }

    fun setTotalTime(totalTime: Int): Timer {
      this.totalTime = totalTime
      return this
    }
  }

}
