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
package org.basinmc.faucet.plugin;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.basinmc.faucet.plugin.dependency.PluginDependency;
import org.basinmc.faucet.plugin.dependency.ServiceDependency;
import org.basinmc.faucet.plugin.dependency.ServiceReference;

/**
 * Represents the metadata associated with a loaded or to-be-loaded plugin.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 * @since 1.0
 */
public interface Plugin {

  /**
   * <p>Retrieves the distribution network identifier for this plugin.</p>
   *
   * <p>This value is used for auto-update and auto-download purposes and may be left empty when
   * the plugin is not available via the distribution network.</p>
   *
   * @return a distribution identifier or an empty optional.
   */
  @NonNull
  Optional<UUID> getDistributionId();

  /**
   * <p>Retrieves the globally unique identification for this plugin.</p>
   *
   * <p>This value is used to refer to this plugin within manifests (for instance, when a
   * dependency is declared) and is expected to be globally unique (e.g. may only ever exist once
   * within the plugin ecosystem).</p>
   *
   * <p>The expected format for this particular field is equal to the Java package format (as
   * outlined in the Java specification and Oracle Code Style). For instance: {@code
   * org.example.project.plugin}</p>
   *
   * @return a unique plugin identifier.
   */
  @NonNull
  String getIdentifier();

  /**
   * <p>Retrieves a human readable (yet machine parsable) representation of the plugin's version
   * number.</p>
   *
   * <p>This value is expected to follow the <a href="https://semver.org">Semantic Versioning</a>
   * specification in order to permit automatic dependency resolving based on compatibility (e.g.
   * evaluate whether an alternative version is expected to be API compatible).</p>
   *
   * @return a plugin version.
   */
  @NonNull
  String getVersion();

  /**
   * Retrieves the phase in which this plugin currently resides.
   *
   * @return a phase.
   */
  @NonNull
  Phase getPhase();

  /**
   * Retrieves a list of services which are provided by this plugin and are made available to
   * plugins which wish to consume them.
   *
   * @return a list of provided services and their versions.
   */
  @NonNull
  List<ServiceReference> getServices();

  /**
   * Retrieves a list of plugin level dependencies which are required to be present in order to
   * permit plugin loading or alter the plugin loading order if present.
   *
   * @return a list of dependencies.
   */
  @NonNull
  List<PluginDependency> getDependencies();

  /**
   * Retrieves a list of services which are required to be present in order to permit plugin loading
   * or alter the plugin loading order if present.
   *
   * @return a list of service dependencies.
   */
  @NonNull
  List<ServiceDependency> getServiceDependencies();

  /**
   * <p>Retrieves a human readable name for this plugin.</p>
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
   * <p>Retrieves a human readable name for this plugin (e.g. a project name or summary of the
   * provided functionality).</p>
   *
   * <p>This value may be localized by the plugin author in order to make their plugins more
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
   * Retrieves a list of authors who are actively involved with the development of this plugin.
   *
   * @return a sorted list of authors.
   */
  @NonNull
  List<String> getAuthors();

  /**
   * Retrieves a list of contributors who contributed to the plugin's development at least once.
   *
   * @return a sorted list of contributors.
   */
  @NonNull
  List<String> getContributors();

  /**
   * Represents the phases which plugins may enter.
   */
  enum Phase {

    /**
     * Plugin has been registered with the server but has not yet been loaded or initialized.
     */
    REGISTERED,

    /**
     * Plugin dependencies have been resolved but it has yet to be loaded and initialized.
     */
    RESOLVED,

    /**
     * Plugin has been loaded (e.g. a class loader has been created) but not yet initialized.
     */
    LOADED,

    /**
     * Plugin is running (e.g. its classes have been registered and initialized (where desired)).
     */
    RUNNING,
  }
}
