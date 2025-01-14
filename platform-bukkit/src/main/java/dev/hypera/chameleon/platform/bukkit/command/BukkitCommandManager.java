/*
 * This file is a part of the Chameleon Framework, licensed under the MIT License.
 *
 * Copyright (c) 2021-2023 The Chameleon Framework Authors.
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
package dev.hypera.chameleon.platform.bukkit.command;

import dev.hypera.chameleon.command.Command;
import dev.hypera.chameleon.command.CommandManager;
import dev.hypera.chameleon.platform.PlatformChameleon;
import dev.hypera.chameleon.platform.bukkit.user.BukkitUserManager;
import dev.hypera.chameleon.util.Preconditions;
import java.lang.reflect.Field;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Bukkit command manager implementation.
 */
@Internal
public final class BukkitCommandManager extends CommandManager {

    private final @NotNull PlatformChameleon<JavaPlugin> chameleon;
    private final @NotNull BukkitUserManager userManager;
    private final @Nullable CommandMap commandMap;

    /**
     * Bukkit command manager constructor.
     *
     * @param chameleon   Bukkit Chameleon implementation.
     * @param userManager Bukkit user manager implementation.
     */
    @Internal
    public BukkitCommandManager(@NotNull PlatformChameleon<JavaPlugin> chameleon, @NotNull BukkitUserManager userManager) {
        super(chameleon);
        this.chameleon = chameleon;
        this.userManager = userManager;

        @Nullable CommandMap map;
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            map = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            chameleon.getInternalLogger().error("Failed to get Bukkit command map", ex);
            map = null;
        }
        this.commandMap = map;
    }

    @Override
    protected void registerCommand(@NotNull Command command) {
        Preconditions.checkState(this.commandMap != null, "commandMap cannot null");
        Objects.requireNonNull(this.commandMap).register(command.getName(),
            this.chameleon.getPlatformPlugin().getName(),
            new BukkitCommand(this.chameleon, this.userManager, command)
        );
    }

    @Override
    protected void unregisterCommand(@NotNull Command command) {
        Preconditions.checkState(this.commandMap != null, "commandMap cannot null");
        org.bukkit.command.Command bukkitCommand = Objects.requireNonNull(this.commandMap)
            .getCommand(command.getName());
        if (bukkitCommand != null) {
            bukkitCommand.unregister(this.commandMap);
        } else {
            throw new IllegalArgumentException("Cannot find command with name '" + command.getName() + "'");
        }
    }

}
