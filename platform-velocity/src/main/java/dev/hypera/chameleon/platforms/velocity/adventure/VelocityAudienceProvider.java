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

package dev.hypera.chameleon.platforms.velocity.adventure;

import dev.hypera.chameleon.core.adventure.ChameleonAudienceProvider;
import dev.hypera.chameleon.core.users.ChatUser;
import dev.hypera.chameleon.core.users.platforms.ProxyUser;
import dev.hypera.chameleon.platforms.velocity.VelocityChameleon;
import dev.hypera.chameleon.platforms.velocity.user.VelocityConsoleUser;
import dev.hypera.chameleon.platforms.velocity.user.VelocityUser;
import dev.hypera.chameleon.platforms.velocity.user.VelocityUsers;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import org.jetbrains.annotations.NotNull;

public class VelocityAudienceProvider implements ChameleonAudienceProvider {

	private final @NotNull VelocityChameleon chameleon;

	public VelocityAudienceProvider(@NotNull VelocityChameleon chameleon) {
		this.chameleon = chameleon;
	}

	@Override
	public @NotNull Audience all() {
		return Audience.audience(players(), console());
	}

	@Override
	public @NotNull Audience console() {
		return new VelocityConsoleUser(chameleon);
	}

	@Override
	public @NotNull Audience players() {
		return Audience.audience(chameleon.getVelocityPlugin().getServer().getAllPlayers().stream().map(p -> new VelocityUser(chameleon, p)).collect(Collectors.toSet()));
	}

	@Override
	public @NotNull Audience player(@NotNull UUID playerId) {
		return VelocityUsers.wrap(chameleon, chameleon.getVelocityPlugin().getServer().getPlayer(playerId).orElseThrow(IllegalStateException::new));
	}

	@Override
	public @NotNull Audience filter(@NotNull Predicate<ChatUser> filter) {
		return all().filterAudience(f -> filter.test((ChatUser) f));
	}

	@Override
	public @NotNull Audience permission(@NotNull String permission) {
		return filter(p -> p.hasPermission(permission));
	}

	@Override
	public @NotNull Audience world(@NotNull Key world) {
		return all();
	}

	@Override
	public @NotNull Audience server(@NotNull String serverName) {
		return filter(p -> p instanceof ProxyUser && null != ((ProxyUser) p).getServer() && ((ProxyUser) p).getServer().getName().equals(serverName));
	}

	@Override
	public @NotNull ComponentFlattener flattener() {
		return ComponentFlattener.basic();
	}

	@Override
	public void close() {

	}

}
