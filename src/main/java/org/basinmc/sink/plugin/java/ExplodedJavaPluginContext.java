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

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.annotation.Nonnull;

/**
 * Provides support for exploded (directory based) JARs which consist of regular bytecode.
 *
 * This loader is mostly relevant to developers as it can be easily integrated with IDEs to speed up
 * the build process further.
 *
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class ExplodedJavaPluginContext extends AbstractJavaPluginContext {
    private final String mainClass;
    private final Path storageDirectory;
    private final PluginMetadata metadata;

    public ExplodedJavaPluginContext(@Nonnull Path source, @Nonnull Path storageDirectory) throws IOException {
        super(source);

        ClassWalker walker = new ClassWalker();
        Files.walkFileTree(this.getSource(), walker);

        this.mainClass = walker.locatorClassVisitor.getClassName().orElseThrow(() -> new IllegalStateException("Could not locate main plugin class"));
        // noinspection OptionalGetWithoutIsPresent
        this.metadata = walker.locatorClassVisitor.getMetadata().get();

        this.storageDirectory = storageDirectory.resolve(this.metadata.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public PluginMetadata getMetadata() {
        return this.metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Path getStorageDirectory() {
        return this.storageDirectory;
    }

    /**
     * Walks a file tree in order to locate the main plugin class.
     */
    static class ClassWalker extends SimpleFileVisitor<Path> {
        private LocatorClassVisitor locatorClassVisitor = new LocatorClassVisitor();

        /**
         * {@inheritDoc}
         */
        @Override
        public FileVisitResult visitFile(@Nonnull Path file, @Nonnull BasicFileAttributes attrs) throws IOException {
            if (!file.getFileName().toString().endsWith(".class")) {
                return FileVisitResult.CONTINUE;
            }

            try (FileInputStream inputStream = new FileInputStream(file.toFile())) {
                ClassReader reader = new ClassReader(inputStream);
                reader.accept(this.locatorClassVisitor, ClassReader.EXPAND_FRAMES);
            }

            return this.locatorClassVisitor.getClassName().map((c) -> FileVisitResult.TERMINATE).orElse(FileVisitResult.CONTINUE);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public FileVisitResult preVisitDirectory(@Nonnull Path dir, @Nonnull BasicFileAttributes attrs) throws IOException {
            if (dir.getFileName().toString().equals("META-INF")) {
                return FileVisitResult.SKIP_SUBTREE;
            }

            return super.preVisitDirectory(dir, attrs);
        }
    }
}
