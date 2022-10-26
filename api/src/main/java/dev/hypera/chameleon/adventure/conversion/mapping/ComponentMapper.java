/*
 * This file is a part of the Chameleon Framework, licensed under the MIT License.
 *
 * Copyright (c) 2021-2022 The Chameleon Framework Authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.hypera.chameleon.adventure.conversion.mapping;

import dev.hypera.chameleon.adventure.conversion.AdventureConverter;
import dev.hypera.chameleon.exceptions.ChameleonRuntimeException;
import java.lang.reflect.Method;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * Maps shaded to platform {@link Component}.
 */
public final class ComponentMapper implements Mapper<Component> {

    private final @NotNull Method gsonSerializerDeserialize;
    private final @NotNull Method gsonSerializerSerialize;
    private final @NotNull Object gsonSerializerInstance;

    /**
     * {@link ComponentMapper} constructor.
     */
    public ComponentMapper() {
        try {
            Class<?> serializerClass = Class.forName(AdventureConverter.PACKAGE + "text.serializer.gson.GsonComponentSerializer");
            this.gsonSerializerDeserialize = serializerClass.getMethod("deserialize", Object.class);
            this.gsonSerializerSerialize = serializerClass.getMethod("serialize", Class.forName(AdventureConverter.PACKAGE + "text.Component"));
            this.gsonSerializerInstance = serializerClass.getMethod("gson").invoke(null);
        } catch (ReflectiveOperationException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Map {@link Component} to the platform version of Adventure.
     *
     * @param component {@link Component} to be mapped.
     *
     * @return Platform instance of {@link Component}.
     */
    @Override
    public @NotNull Object map(@NotNull Component component) {
        String json = GsonComponentSerializer.gson().serialize(component);
        try {
            return this.gsonSerializerDeserialize.invoke(this.gsonSerializerInstance, json);
        } catch (ReflectiveOperationException ex) {
            throw new ChameleonRuntimeException(ex);
        }
    }

    /**
     * Map platform {@link Component} to shaded instance.
     *
     * @param component Platform {@link Component} to be mapped.
     *
     * @return Shaded instance of {@link Component}.
     */
    public @NotNull Component mapBackwards(@NotNull Object component) {
        try {
            Object json = this.gsonSerializerSerialize.invoke(this.gsonSerializerInstance, component);
            return GsonComponentSerializer.gson().deserialize((String) json);
        } catch (ReflectiveOperationException ex) {
            throw new ChameleonRuntimeException(ex);
        }
    }

}