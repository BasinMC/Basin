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
package org.basinmc.faucet.world;

/**
 * Represents a generic type of block. TODO My suggestion for this would be to find a way to
 * automatically extract this data from the NMS Block classes.
 */
public final class BlockType {

  private final boolean isFullBlock;
  private final boolean translucent;
  private final boolean isTileEntity;
  private final boolean randomTick;
  private final float hardness;
  private final float explosionResistance;
  private final float slide;
  private final int luminosity;
  private final String name;

  public BlockType(boolean isFullBlock, boolean translucent, boolean isTileEntity,
      boolean randomTick, float hardness, float explosionResistance, float slide, int luminosity,
      String name) {
    this.isFullBlock = isFullBlock;
    this.translucent = translucent;
    this.isTileEntity = isTileEntity;
    this.randomTick = randomTick;
    this.hardness = hardness;
    this.explosionResistance = explosionResistance;
    this.slide = slide;
    this.luminosity = luminosity;
    this.name = name;
  }

  public boolean isFullBlock() {
    return isFullBlock;
  }

  public boolean isTranslucent() {
    return translucent;
  }

  public boolean isTileEntity() {
    return isTileEntity;
  }

  public boolean doesRandomTick() {
    return randomTick;
  }

  public float getHardness() {
    return hardness;
  }

  public float getExplosionResistance() {
    return explosionResistance;
  }

  public float getSlide() {
    return slide;
  }

  public int getLuminosity() {
    return luminosity;
  }

  public String getName() {
    return name;
  }
}
