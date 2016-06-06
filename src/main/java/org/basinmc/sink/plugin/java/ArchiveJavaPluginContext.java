/*
 * Copyright 2016 Johannes Donath <johannesd@torchmind.com>
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
package org.basinmc.sink.plugin.java;

import org.basinmc.faucet.plugin.PluginMetadata;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Nonnull;

/**
 * Provides an archive based Java plugin loader capable of loading JAR or ZIP files which contain
 * a set of bytecode classes.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class ArchiveJavaPluginContext extends AbstractJavaPluginContext {
    private final String mainClass;
    private final PluginMetadata metadata;

    public ArchiveJavaPluginContext(@Nonnull Path source, @Nonnull Path storageDirectory) throws IOException {
        super(source, storageDirectory);

        LocatorClassVisitor locatorClassVisitor = new LocatorClassVisitor();
        String mainClass = null;
        PluginMetadata metadata = null;

        try (ZipFile file = new ZipFile(source.toFile())) {
            Enumeration<? extends ZipEntry> enumeration = file.entries();

            while (enumeration.hasMoreElements()) {
                ZipEntry entry = enumeration.nextElement();

                if (entry.isDirectory() || !entry.getName().endsWith(".class") || entry.getName().startsWith("META-INF")) {
                    continue;
                }

                try (InputStream inputStream = file.getInputStream(entry)) {
                    ClassReader reader = new ClassReader(inputStream);
                    reader.accept(locatorClassVisitor, ClassReader.EXPAND_FRAMES);

                    if (locatorClassVisitor.getMetadata().isPresent()) {
                        // noinspection OptionalGetWithoutIsPresent
                        mainClass = locatorClassVisitor.getClassName().get();
                        // noinspection OptionalGetWithoutIsPresent
                        metadata = locatorClassVisitor.getMetadata().get();
                    }
                }
            }
        }

        if (mainClass == null || metadata == null) {
            throw new IllegalArgumentException("No main class found in archive");
        }

        this.mainClass = mainClass;
        this.metadata = metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public PluginMetadata getMetadata() {
        return this.metadata;
    }
}
