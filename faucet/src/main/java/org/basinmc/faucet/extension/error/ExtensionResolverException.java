/*
 * Copyright 2019 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.faucet.extension.error;

import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.List;
import java.util.stream.Collectors;
import org.basinmc.faucet.extension.Extension;
import org.basinmc.faucet.extension.dependency.ExtensionDependency;
import org.basinmc.faucet.extension.dependency.ServiceDependency;
import org.basinmc.faucet.extension.manifest.ExtensionAuthor;
import org.basinmc.faucet.extension.manifest.ExtensionManifest;

/**
 * Notifies the caller about unresolved dependencies which prevent an extension from starting.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class ExtensionResolverException extends ExtensionContainerException {

  public ExtensionResolverException(@NonNull ExtensionManifest manifest,
      @NonNull List<ExtensionDependency> extensionDependencies,
      @NonNull List<ServiceDependency> serviceDependencies) {
    super(buildErrorMessage(manifest, extensionDependencies, serviceDependencies));
  }

  public ExtensionResolverException(@NonNull ExtensionManifest manifest,
      @NonNull List<? extends Extension> extensions) {
    super(buildErrorMessage(manifest, extensions));
  }

  @NonNull
  private static String buildErrorMessage(@NonNull ExtensionManifest manifest,
      @NonNull List<ExtensionDependency> extensionDependencies,
      @NonNull List<ServiceDependency> serviceDependencies) {
    var builder = new StringBuilder()
        .append("Cannot resolve one or more dependencies for extension")
        .append(manifest.getIdentifier())
        .append(" v").append(manifest.getVersion())
        .append(System.lineSeparator())
        .append(System.lineSeparator());

    appendExtensionMetadata(builder, manifest);

    if (!extensionDependencies.isEmpty()) {
      builder.append("=== Unresolved Extension Dependencies ===").append(System.lineSeparator());
      extensionDependencies.forEach((ext) -> builder.append(" - ")
          .append(ext.getIdentifier())
          .append(" v").append(ext.getVersionRange())
          .append(System.lineSeparator()));
      builder.append(System.lineSeparator());
    }

    if (!serviceDependencies.isEmpty()) {
      builder.append("=== Unresolved Service Dependencies ==").append(System.lineSeparator());
      serviceDependencies.forEach((srv) -> builder.append(" - ")
          .append(srv.getBaseClassName())
          .append(" v").append(srv.getVersionRange())
          .append(System.lineSeparator()));
      builder.append(System.lineSeparator());
    }

    appendFooter(builder, manifest);

    return builder.toString();
  }

  @NonNull
  private static String buildErrorMessage(@NonNull ExtensionManifest manifest,
      @NonNull List<? extends Extension> extensions) {
    var builder = new StringBuilder()
        .append("Cannot resolve one or more dependencies for extension")
        .append(manifest.getIdentifier())
        .append(" v").append(manifest.getVersion())
        .append(System.lineSeparator())
        .append(System.lineSeparator());

    appendExtensionMetadata(builder, manifest);

    builder.append("=== Unresolved Extensions ===");
    extensions.forEach((e) -> builder.append(" - ")
        .append(e.getManifest().getIdentifier())
        .append(" v").append(e.getManifest().getVersion())
        .append(System.lineSeparator()));
    builder.append(System.lineSeparator());

    appendFooter(builder, manifest);

    return builder.toString();
  }

  private static void appendExtensionMetadata(@NonNull StringBuilder builder,
      @NonNull ExtensionManifest manifest) {
    builder.append("=== Metadata ===").append(System.lineSeparator())
        .append("  Extension Id: ").append(manifest.getIdentifier()).append(System.lineSeparator())
        .append("  Version: ").append(manifest.getVersion()).append(System.lineSeparator())
        .append("  Display Name: ").append(manifest.getDisplayName()).append(System.lineSeparator())
        .append("  Author(s): ").append(
        manifest.getAuthors().stream().map(ExtensionAuthor::toString)
            .collect(Collectors.joining(", "))).append(System.lineSeparator())
        .append("  Contributor(s): ").append(
        manifest.getContributors().stream().map(ExtensionAuthor::toString)
            .collect(Collectors.joining(", "))).append(System.lineSeparator())
        .append(System.lineSeparator());
  }

  private static void appendFooter(@NonNull StringBuilder builder,
      @NonNull ExtensionManifest manifest) {
    // TODO: Refer to documentation link when available
    builder.append("This extension will not load until these dependencies are made available.")
        .append(System.lineSeparator());
    builder.append("Please refer to the extension documentation for more information");
  }
}
