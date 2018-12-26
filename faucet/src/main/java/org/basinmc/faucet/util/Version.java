/*
 * Copyright 2018 Johannes Donath  <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package org.basinmc.faucet.util;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public class Version implements Comparable<Version> {

  /**
   * Pattern with which semantic versions may be validated or separated into their respective
   * components.
   */
  // 1  => Major
  // 2  => Major (non-zero)
  // 3  => Minor
  // 4  => Minor (non-zero)
  // 5  => Patch
  // 6  => Patch (non-zero)
  // 7  => Extra (with separator)
  // 8  => Extra
  // 9  => Metadata (with separator)
  // 10 => Metadata
  public static final Pattern PATTERN = Pattern.compile(
      "^(([1-9][0-9]*)|0)\\.(([1-9][0-9]*)|0)\\.(([1-9][0-9]*)|0)(-([A-Z0-9-.]+))?(\\+([A-Z0-9-.]+))?$",
      Pattern.CASE_INSENSITIVE);

  private final int major;
  private final int minor;
  private final int patch;
  private final String extra;
  private final String buildMetadata;

  private final Stability stability;
  private final int stabilityIndex;

  public Version(@NonNull String version) {
    var matcher = PATTERN.matcher(version);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Malformed version number: " + version);
    }

    try {
      this.major = Integer.parseUnsignedInt(matcher.group(1));
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Malformed major bit: " + matcher.group(1), ex);
    }

    try {
      this.minor = Integer.parseUnsignedInt(matcher.group(3));
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Malformed minor bit: " + matcher.group(2), ex);
    }

    try {
      this.patch = Integer.parseUnsignedInt(matcher.group(5));
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Malformed patch bit: " + matcher.group(3), ex);
    }

    this.extra = matcher.group(8);
    this.buildMetadata = matcher.group(10);

    if (this.extra == null) {
      this.stability = Stability.STABLE;
      this.stabilityIndex = 0;
    } else {
      var elements = this.extra.split("\\.");
      var stabilityIndex = 0;
      if (elements.length >= 2) {
        try {
          stabilityIndex = Integer.parseUnsignedInt(elements[1]);
        } catch (NumberFormatException ignore) {
        }
      }

      this.stability = Stability.byKeyword(elements[0])
          .orElse(Stability.UNKNOWN);
      this.stabilityIndex = stabilityIndex;
    }
  }

  public int major() {
    return this.major;
  }

  public int minor() {
    return this.minor;
  }

  public int patch() {
    return this.patch;
  }

  @NonNull
  public Optional<String> extra() {
    return Optional.ofNullable(this.extra);
  }

  @NonNull
  public Optional<String> buildMetadata() {
    return Optional.ofNullable(this.buildMetadata);
  }

  @NonNull
  public Stability stability() {
    return this.stability;
  }

  public int stabilityIndex() {
    return this.stabilityIndex;
  }

  /**
   * Evaluates whether this version is newer than the passed version.
   *
   * @param version a version.
   * @return true if this version is newer, false otherwise.
   */
  public boolean isNewerThan(@NonNull Version version) {
    if (this == version) {
      return false;
    }

    if (this.major > version.major) {
      return true;
    }
    if (this.major < version.major) {
      return false;
    }
    if (this.minor > version.minor) {
      return true;
    }
    if (this.minor < version.minor) {
      return false;
    }
    if (this.patch > version.patch) {
      return true;
    }
    if (this.patch < version.patch) {
      return false;
    }
    if (this.stability.ordinal() > version.stability.ordinal()) {
      return true;
    }
    if (this.stability.ordinal() < version.stability.ordinal()) {
      return false;
    }
    if (this.stabilityIndex > version.stabilityIndex) {
      return true;
    }
    if (this.stabilityIndex < version.stabilityIndex) {
      return false;
    }

    return false;
  }

  /**
   * Evaluates whether this version is older than the passed version.
   *
   * @param version a version.
   * @return true if this version is older, false otherwise.
   */
  public boolean isOlderThan(@NonNull Version version) {
    if (this == version) {
      return false;
    }

    if (this.major > version.major) {
      return false;
    }
    if (this.major < version.major) {
      return true;
    }
    if (this.minor > version.minor) {
      return false;
    }
    if (this.minor < version.minor) {
      return true;
    }
    if (this.patch > version.patch) {
      return false;
    }
    if (this.patch < version.patch) {
      return true;
    }
    if (this.stability.ordinal() > version.stability.ordinal()) {
      return false;
    }
    if (this.stability.ordinal() < version.stability.ordinal()) {
      return true;
    }
    if (this.stabilityIndex > version.stabilityIndex) {
      return false;
    }
    return this.stabilityIndex < version.stabilityIndex;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(@NonNull Version o) {
    if (this.equals(o)) {
      return 0;
    }

    return this.isNewerThan(o) ? 1 : -1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Version)) {
      return false;
    }
    Version version = (Version) o;
    return this.major == version.major &&
        this.minor == version.minor &&
        this.patch == version.patch &&
        Objects.equals(this.extra, version.extra);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.major, this.minor, this.patch, this.extra);
  }

  /**
   * Provides a list of valid version stability levels.
   */
  public enum Stability {
    ALPHA("a", "alpha"),
    BETA("b", "beta"),
    RELEASE_CANDIDATE("rc"),
    UNKNOWN,
    STABLE;

    private static final Map<String, Stability> KEYWORD_MAP = new HashMap<>();
    private final Set<String> keywords;

    static {
      for (var stability : values()) {
        stability.keywords.forEach((k) -> KEYWORD_MAP.put(k, stability));
      }
    }

    Stability(@NonNull String... keywords) {
      this.keywords = Set.of(keywords);
    }

    @NonNull
    public Set<String> keywords() {
      return this.keywords;
    }

    @NonNull
    public static Optional<Stability> byKeyword(@NonNull String keyword) {
      return Optional.ofNullable(KEYWORD_MAP.get(keyword.toLowerCase()));
    }
  }
}
