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

package dev.hypera.chameleon.platforms.spigot;

import dev.hypera.chameleon.core.Chameleon;
import dev.hypera.chameleon.core.ChameleonPlugin;
import dev.hypera.chameleon.core.adventure.ChameleonAudienceProvider;
import dev.hypera.chameleon.core.exceptions.instantiation.ChameleonInstantiationException;
import dev.hypera.chameleon.core.managers.CommandManager;
import dev.hypera.chameleon.core.managers.PluginManager;
import dev.hypera.chameleon.core.managers.Scheduler;
import dev.hypera.chameleon.core.managers.UserManager;
import dev.hypera.chameleon.core.platform.Platform;
import dev.hypera.chameleon.platforms.spigot.adventure.SpigotAudienceProvider;
import dev.hypera.chameleon.platforms.spigot.events.SpigotListener;
import dev.hypera.chameleon.platforms.spigot.managers.SpigotCommandManager;
import dev.hypera.chameleon.platforms.spigot.managers.SpigotPluginManager;
import dev.hypera.chameleon.platforms.spigot.managers.SpigotScheduler;
import dev.hypera.chameleon.platforms.spigot.managers.SpigotUserManager;
import dev.hypera.chameleon.platforms.spigot.platform.SpigotPlatform;
import java.nio.file.Path;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Spigot Chameleon
 */
public final class SpigotChameleon extends Chameleon {

	private final @NotNull JavaPlugin plugin;
	private final @NotNull ChameleonAudienceProvider audienceProvider;
	private final @NotNull SpigotPlatform platform = new SpigotPlatform();
	private final @NotNull SpigotCommandManager commandManager = new SpigotCommandManager(this);
	private final @NotNull SpigotPluginManager pluginManager = new SpigotPluginManager();
	private final @NotNull SpigotUserManager userManager = new SpigotUserManager(this);
	private final @NotNull SpigotScheduler scheduler = new SpigotScheduler(this);

	public SpigotChameleon(@NotNull Class<? extends ChameleonPlugin> chameleonPlugin, @NotNull JavaPlugin spigotPlugin) throws ChameleonInstantiationException {
		super(chameleonPlugin);
		this.plugin = spigotPlugin;
		this.audienceProvider = new SpigotAudienceProvider(this, spigotPlugin);
		Bukkit.getPluginManager().registerEvents(new SpigotListener(this), plugin);
	}



	@Override
	public @NotNull ChameleonAudienceProvider getAdventure() {
		return audienceProvider;
	}

	@Override
	public @NotNull Platform getPlatform() {
		return platform;
	}


	@Override
	public @NotNull CommandManager getCommandManager() {
		return commandManager;
	}

	@Override
	public @NotNull PluginManager getPluginManager() {
		return pluginManager;
	}

	@Override
	public @NotNull UserManager getUserManager() {
		return userManager;
	}

	@Override
	public @NotNull Scheduler getScheduler() {
		return scheduler;
	}


	@Override
	public @NotNull Path getDataFolder() {
		return plugin.getDataFolder().toPath().toAbsolutePath();
	}


	public @NotNull JavaPlugin getSpigotPlugin() {
		return plugin;
	}

}