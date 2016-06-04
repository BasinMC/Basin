/*
 * Copyright 2016 __0x277F <0x277F@gmail.com>
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
 */

package org.basinmc.faucet.plugin;

import java.util.Map;
import java.util.Optional;

/**
 * Holds data on a plugin relevant to it loading.
 */
public final class PluginMetadata {
    /**
     * A human-readable name of the plugin. Can have spaces.
     */
    private final String name;
    /**
     * A unique ID string for this plugin. Should have no spaces.
     */
    private final String id;
    /**
     * Semantic versioning of the plugin
     */
    private final PluginVersion version;
    /**
     * A short description of the plugin's purpose. May be omitted.
     */
    private final Optional<String> desc;
    /**
     * A map between plugin <i>ID</i>s and acceptable version ranges. Plugins specified in this map
     * will be (attempted to be) loaded prior to this one. If a dependency is not met, the plugin
     * will be disabled.
     */
    private final Map<String, VersionRange> dependencies;
    /**
     * A map between plugin <i>ID</i>s and acceptable version ranges. Plugins specified in this map
     * will be (attempted to be) loaded prior to this one. If a dependency is not met, the plugin
     * will <b>NOT</b> be disabled but rather a warning will be outputted.
     */
    private final Map<String, VersionRange> softDependencies;
    /**
     * Represents the main class of the plugin.
     */
    private final PluginInstance mainClass;

    public PluginMetadata(String name, String id, PluginVersion version, String desc, Map<String, VersionRange> dependencies, Map<String, VersionRange> softDependencies, PluginInstance mainClass) {
        this.name = name;
        this.id = id;
        this.version = version;
        this.desc = Optional.ofNullable(desc);
        this.dependencies = dependencies;
        this.softDependencies = softDependencies;
        this.mainClass = mainClass;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public PluginVersion getVersion() {
        return version;
    }

    public Optional<String> getDesc() {
        return desc;
    }

    public Map<String, VersionRange> getDependencies() {
        return dependencies;
    }

    public Map<String, VersionRange> getSoftDependencies() {
        return softDependencies;
    }

    public PluginInstance getMainClass() {
        return mainClass;
    }
}
