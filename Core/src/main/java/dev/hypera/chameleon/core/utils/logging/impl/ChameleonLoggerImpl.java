/*
 * Chameleon - Cross-platform Minecraft plugin framework
 * Copyright (c) 2021 Joshua Sing <joshua@hypera.dev>
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

package dev.hypera.chameleon.core.utils.logging.impl;

import dev.hypera.chameleon.core.Chameleon;
import dev.hypera.chameleon.core.utils.logging.ChameleonLogger;
import dev.hypera.chameleon.core.utils.string.ImprovedStringBuilder;
import dev.hypera.chameleon.core.utils.string.StringUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class ChameleonLoggerImpl implements ChameleonLogger {

	private static final String RESET = "&r";
	private static final String CHAMELEON_PREFIX = "&b[Chameleon]";
	private static final String DEBUG_PREFIX = "&c[DEBUG] ";
	private static final String WARNING_PREFIX = "&e[WARNING] ";
	private static final String ERROR_PREFIX = "&4[ERROR] ";

	private final @NotNull Chameleon chameleon;
	private final boolean isChameleon;
	private boolean debug = false;

	public ChameleonLoggerImpl(Class<?> clazz, @NotNull Chameleon chameleon) {
		this.chameleon = chameleon;
		this.isChameleon = clazz.getPackage().getName().startsWith("dev.hypera.chameleon");
	}


	@Override
	public void log(String s) {
		ImprovedStringBuilder builder = StringUtils.getImprovedStringBuilder();
		builder.appendIfElse(String.format(chameleon.getPlugin().getData().getLogPrefix(), chameleon.getPlugin().getData().getName()), CHAMELEON_PREFIX, (str) -> !isChameleon).append(RESET).append(" ")
				.append(s);
		chameleon.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(builder.toString()));
	}

	@Override
	public void info(String s, Object... o) {
		log(String.format(s, o));
	}

	@Override
	public void debug(String s, Object... o) {
		log(DEBUG_PREFIX + String.format(s, o));
	}

	@Override
	public void warn(String s, Object... o) {
		log(WARNING_PREFIX + String.format(s, o));
	}

	@Override
	public void warn(String s, Throwable throwable, Object... o) {
		warn(s, o);
		warn(getTrace(throwable));
	}

	@Override
	public void error(String s, Object... o) {
		log(ERROR_PREFIX + String.format(s, o));
	}

	@Override
	public void error(String s, Throwable throwable, Object... o) {
		error(s, o);
		error(getTrace(throwable));
	}

	@Override
	public void setDebugEnabled(boolean enabled) {
		this.debug = enabled;
	}

	@Override
	public boolean isDebugEnabled() {
		return debug;
	}

	private String getTrace(Throwable throwable) {
		try (StringWriter writer = new StringWriter(); PrintWriter printWriter = new PrintWriter(writer)) {
			throwable.printStackTrace(printWriter);
			return writer.toString();
		} catch (Exception ignored) {
			return null;
		}
	}

}
