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
package dev.hypera.chameleon.platform.bungeecord.user;

import dev.hypera.chameleon.platform.user.PlatformChatUser;
import dev.hypera.chameleon.user.ConsoleUser;
import dev.hypera.chameleon.util.Preconditions;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

/**
 * BungeeCord console user implementation.
 */
public final class BungeeCordConsoleUser extends PlatformChatUser implements ConsoleUser, ForwardingAudience.Single {

    private final @NotNull Audience audience;

    /**
     * BungeeCord console user.
     *
     * @param consoleAudience Adventure console audience.
     */
    @Internal
    BungeeCordConsoleUser(@NotNull Audience consoleAudience) {
        this.audience = consoleAudience;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(@NotNull String permission) {
        Preconditions.checkNotNull("permission", permission);
        return ProxyServer.getInstance().getConsole().hasPermission(permission);
    }

    /**
     * Returns the audience for this user.
     *
     * @return audience.
     */
    @Override
    public @NotNull Audience audience() {
        return this.audience;
    }

}
