/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.basinmc.faucet.plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides a list of valid unstable version types.
 *
 * @author Johannes Donath
 */
public enum UnstableVersionType implements Comparable<UnstableVersionType> {
    UNKNOWN,
    SNAPSHOT("snapshot"),
    ALPHA("a", "alpha"),
    BETA("b", "beta"),
    RELEASE_CANDIDATE("rc");

    private static final Map<String, UnstableVersionType> aliasMap;

    static {
        Map<String, UnstableVersionType> map = new HashMap<>();

        for (UnstableVersionType versionType : values()) {
            if (versionType.aliases == null) {
                continue;
            }

            for (String alias : versionType.aliases) {
                map.putIfAbsent(alias, versionType);
            }
        }

        aliasMap = Collections.unmodifiableMap(map);
    }

    private final String[] aliases;

    UnstableVersionType(@Nullable String... aliases) {
        this.aliases = aliases;
    }

    /**
     * Retrieves the version type of a certain identifier.
     *
     * @param alias The alias.
     * @return The version type.
     */
    @Nonnull
    public static Optional<UnstableVersionType> byAlias(@Nullable String alias) {
        if (alias == null) {
            return Optional.empty();
        }
        return Optional.of(aliasMap.getOrDefault(alias, UNKNOWN));
    }
}