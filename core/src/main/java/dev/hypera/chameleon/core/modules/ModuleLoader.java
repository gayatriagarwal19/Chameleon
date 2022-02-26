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

package dev.hypera.chameleon.core.modules;

import dev.hypera.chameleon.core.Chameleon;
import dev.hypera.chameleon.core.annotations.InjectModule;
import dev.hypera.chameleon.core.modules.platform.PlatformModuleLoader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

public class ModuleLoader {

	private final @NotNull Chameleon chameleon;
	private final @NotNull PlatformModuleLoader platformModuleLoader;

	@Internal
	public ModuleLoader(@NotNull Chameleon chameleon) {
		this.chameleon = chameleon;
		this.platformModuleLoader = new PlatformModuleLoader(chameleon);
	}


	public void injectModule(@NotNull Field field, @NotNull Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		if (field.isAnnotationPresent(InjectModule.class) && Module.class.isAssignableFrom(field.getType())) {
			field.setAccessible(true);
			if (null == field.get(obj)) {
				field.set(obj, loadModule(field.getType().asSubclass(Module.class)));
			}
		}
	}


	private @NotNull Module loadModule(@NotNull Class<? extends Module> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		return clazz.getConstructor(Chameleon.class, PlatformModuleLoader.class).newInstance(chameleon, platformModuleLoader);
	}

}
