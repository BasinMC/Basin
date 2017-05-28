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
 * Represents a biome type.
 */
public class Biome {
  // TODO - auto-generate constants here from NMS code

  private final String name;
  private float temperature = 0.5F;
  private float rainfall = 0.5F;

  // TODO figure out what these two do
  private float baseHeight = 0.1F;
  private float heightVariation = 0.2F;

  public Biome(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public float getTemperature() {
    return temperature;
  }

  public float getRainfall() {
    return rainfall;
  }

  public Biome setTemperature(float temperature) {
    this.temperature = temperature;
    return this;
  }

  public Biome setRainfall(float rainfall) {
    this.rainfall = rainfall;
    return this;
  }

  public float getBaseHeight() {
    return baseHeight;
  }

  public Biome setBaseHeight(float baseHeight) {
    this.baseHeight = baseHeight;
    return this;
  }

  public float getHeightVariation() {
    return heightVariation;
  }

  public Biome setHeightVariation(float heightVariation) {
    this.heightVariation = heightVariation;
    return this;
  }
}
