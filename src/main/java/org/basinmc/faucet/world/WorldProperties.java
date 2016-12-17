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
package org.basinmc.faucet.world;

import org.basinmc.faucet.math.BlockAlignedVector3;
import org.basinmc.faucet.math.Vector2I;

/**
 * Represents various properties about a world. Default values are taken from
 * net.minecraft.world.storage.WorldInfo
 */
public class WorldProperties {
    private int seaLevel = 63;
    private Difficulty difficulty = Difficulty.NORMAL;
    private BlockAlignedVector3 spawn;
    private final Timer timer = new Timer();
    private final int dimensionId;
    private String name;
    private boolean hardcore = false;
    private Gamemode defaultGamemode = Gamemode.SURVIVAL;
    private boolean borderEnabled = false;
    private Border worldBorder = new Border();
    private int rainTime = 0;

    public WorldProperties(BlockAlignedVector3 spawn, int dimensionId, String name) {
        this.spawn = spawn;
        this.dimensionId = dimensionId;
        this.name = name;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public WorldProperties setSeaLevel(int seaLevel) {
        this.seaLevel = seaLevel;
        return this;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public WorldProperties setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public BlockAlignedVector3 getSpawn() {
        return spawn;
    }

    public WorldProperties setSpawn(BlockAlignedVector3 spawn) {
        this.spawn = spawn;
        return this;
    }

    public Timer getTimer() {
        return timer;
    }

    public int getDimensionId() {
        return dimensionId;
    }

    public String getName() {
        return name;
    }

    public WorldProperties setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isHardcore() {
        return hardcore;
    }

    public WorldProperties setHardcore(boolean hardcore) {
        this.hardcore = hardcore;
        return this;
    }

    public Gamemode getDefaultGamemode() {
        return defaultGamemode;
    }

    public WorldProperties setDefaultGamemode(Gamemode defaultGamemode) {
        this.defaultGamemode = defaultGamemode;
        return this;
    }

    public boolean isBorderEnabled() {
        return borderEnabled;
    }

    public WorldProperties setBorderEnabled(boolean borderEnabled) {
        this.borderEnabled = borderEnabled;
        return this;
    }

    public Border getWorldBorder() {
        return worldBorder;
    }

    public WorldProperties setWorldBorder(Border worldBorder) {
        this.worldBorder = worldBorder;
        return this;
    }

    public int getRainTime() {
        return rainTime;
    }

    public WorldProperties setRainTime(int rainTime) {
        this.rainTime = rainTime;
        return this;
    }

    /**
     * Properties relating to a world border. Default values are taken from net.minecraft.world.storage.WorldInfo
     */
    public static class Border {
        private Vector2I center = new Vector2I(0, 0);
        private double radius = 6.0E7D;
        private double safeZone = 5.0D;
        private double damage = 0.2D;
        private int warningDistance = 5;
        private int warningTime = 15;

        /**
         * Get the block coordinates of the center of the world border
         *
         * @return a 2 dimensional integer vector
         */
        public Vector2I getCenter() {
            return center;
        }

        public Border setCenter(Vector2I center) {
            this.center = center;
            return this;
        }

        /**
         * Get the distance which the world border extends in each direction from the center.
         *
         * @return distance, in blocks
         */
        public double getRadius() {
            return radius;
        }

        public Border setRadius(double radius) {
            this.radius = radius;
            return this;
        }

        public double getSafeZone() {
            return safeZone;
        }

        public Border setSafeZone(double safeZone) {
            this.safeZone = safeZone;
            return this;
        }

        /**
         * Get the amount of damage dealt per block to players who cross the border
         */
        public double getDamage() {
            return damage;
        }

        public Border setDamage(double damage) {
            this.damage = damage;
            return this;
        }

        public int getWarningDistance() {
            return warningDistance;
        }

        public Border setWarningDistance(int warningDistance) {
            this.warningDistance = warningDistance;
            return this;
        }

        public int getWarningTime() {
            return warningTime;
        }

        public Border setWarningTime(int warningTime) {
            this.warningTime = warningTime;
            return this;
        }
    }


    public static class Timer {
        private int totalTime = 0;
        private int localTime = 0;

        public int getLocalTime() {
            return localTime;
        }

        public Timer setLocalTime(int localTime) {
            this.localTime = localTime;
            return this;
        }

        public int getTotalTime() {
            return totalTime;
        }

        public Timer setTotalTime(int totalTime) {
            this.totalTime = totalTime;
            return this;
        }
    }

}
