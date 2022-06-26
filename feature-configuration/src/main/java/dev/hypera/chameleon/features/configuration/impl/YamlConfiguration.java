/*
 * Chameleon Framework - Cross-platform Minecraft plugin framework
 *  Copyright (c) 2021-present The Chameleon Framework Authors.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package dev.hypera.chameleon.features.configuration.impl;

import dev.hypera.chameleon.features.configuration.Configuration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

/**
 * YAML {@link Configuration} implementation.
 */
public class YamlConfiguration extends Configuration {

    private static final @NotNull Yaml yaml = new Yaml();

    private @Nullable Map<String, Object> config;

    /**
     * {@link YamlConfiguration} constructor.
     *
     * @param dataFolder Folder to store loaded configuration file in.
     * @param fileName   Configuration file name.
     */
    public YamlConfiguration(@NotNull Path dataFolder, @NotNull String fileName) {
        super(dataFolder, fileName);
    }

    /**
     * {@link YamlConfiguration} constructor.
     *
     * @param dataFolder               Folder to store loaded configuration file in.
     * @param fileName                 Configuration file name.
     * @param copyDefaultFromResources Whether this file should be copied from resources if not already loaded.
     */
    public YamlConfiguration(@NotNull Path dataFolder, @NotNull String fileName, boolean copyDefaultFromResources) {
        super(dataFolder, fileName, copyDefaultFromResources);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Configuration load() throws IOException {
        if (!Files.exists(dataFolder)) {
            Files.createDirectories(dataFolder);
        }

        if (!Files.exists(path)) {
            if (copyDefaultFromResources) {
                try (InputStream defaultResource = YamlConfiguration.class.getResourceAsStream("/" + fileName)) {
                    if (null == defaultResource) {
                        throw new IllegalStateException("Failed to load resource '" + fileName + "'");
                    }

                    Files.copy(defaultResource, path);
                }
            } else {
                Files.createFile(path);
            }
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            this.config = yaml.load(reader);
            this.loaded = true;
        }

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Configuration unload() {
        this.loaded = false;
        this.config = null;
        return this;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Optional<Object> get(@NotNull String path) {
        if (!this.loaded || null == this.config) {
            throw new IllegalStateException("Configuration has not been loaded");
        }

        return getObject(path, this.config);
    }

}
