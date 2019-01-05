/*
 * Copyright 2018 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.faucet.extension.manifest;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.List;
import java.util.Locale;
import org.basinmc.faucet.extension.dependency.ExtensionDependency;
import org.basinmc.faucet.extension.dependency.ServiceDependency;
import org.basinmc.faucet.extension.dependency.ServiceVersion;
import org.basinmc.faucet.util.Version;

/**
 * Represents the extension's identifying information (such as identifier, version, display name,
 * authors, etc) as well as its dependencies and provided services.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public interface ExtensionManifest {

  /**
   * Defines a magic number with which the extension container header is introduced.
   */
  int MAGIC_NUMBER = 0x0DEBAC1E;

  /**
   * <p>Retrieves the manifest format version.</p>
   *
   * <p>In cases where multiple revisions are supported by the server implementation, this value
   * may be useful in providing fallback functionality where necessary.</p>
   *
   * @return a manifest version.
   */
  int getFormatVersion();

  /**
   * Retrieves a list of set extension flags.
   *
   * @return a list of flags.
   */
  @NonNull
  ExtensionFlags getFlags();

  /**
   * <p>Retrieves the globally unique identification for this extension.</p>
   *
   * <p>This value is used to refer to this extension within manifests (for instance, when a
   * dependency is declared) and is expected to be globally unique (e.g. may only ever exist once
   * within the extension ecosystem).</p>
   *
   * <p>The expected format for this particular field is equal to the Java package format (as
   * outlined in the Java specification and Oracle Code Style). For instance: {@code
   * org.example.project.extension}</p>
   *
   * @return a unique extension identifier.
   */
  @NonNull
  String getIdentifier();

  /**
   * <p>Retrieves a human readable (yet machine parsable) representation of the extension's version
   * number.</p>
   *
   * <p>This value is expected to follow the <a href="https://semver.org">Semantic Versioning</a>
   * specification in order to permit automatic dependency resolving based on compatibility (e.g.
   * evaluate whether an alternative version is expected to be API compatible).</p>
   *
   * @return a extension version.
   */
  @NonNull
  Version getVersion();

  /**
   * Retrieves a list of services which are provided by this extension and are made available to
   * plugins which wish to consume them.
   *
   * @return a list of provided services and their versions.
   */
  @NonNull
  List<ServiceVersion> getServices();

  /**
   * Retrieves a list of extension level dependencies which are required to be present in order to
   * permit extension loading or alter the extension loading order if present.
   *
   * @return a list of dependencies.
   */
  @NonNull
  List<ExtensionDependency> getExtensionDependencies();

  /**
   * Retrieves a list of services which are required to be present in order to permit extension
   * loading or alter the extension loading order if present.
   *
   * @return a list of service dependencies.
   */
  @NonNull
  List<ServiceDependency> getServiceDependencies();

  /**
   * <p>Retrieves a human readable name for this extension.</p>
   *
   * <p>This method behaves the same way as {@link #getDisplayName(Locale)} but will always choose
   * the current server locale as its display locale.</p>
   *
   * @return a display name.
   * @see #getDisplayName(Locale)
   */
  @NonNull
  String getDisplayName();

  /**
   * <p>Retrieves a human readable name for this extension (e.g. a project name or summary of the
   * provided functionality).</p>
   *
   * <p>This value may be localized by the extension author in order to make their plugins more
   * accessible.</p>
   *
   * <p>When no localization has been declared for the specified display locale, a standard
   * translation will be returned instead.</p>
   *
   * @param locale an arbitrary display locale.
   * @return a localized name.
   */
  @NonNull
  String getDisplayName(@NonNull Locale locale);

  /**
   * Retrieves a list of authors who are actively involved with the development of this extension.
   *
   * @return a sorted list of authors.
   */
  @NonNull
  List<ExtensionAuthor> getAuthors();

  /**
   * Retrieves a list of contributors who contributed to the extension's development at least once.
   *
   * @return a sorted list of contributors.
   */
  @NonNull
  List<ExtensionAuthor> getContributors();
}
