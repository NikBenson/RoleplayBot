package com.github.nikbenson.roleplaybot.modules;

import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class ModulesManager {
	private static final Set<Class<? extends RoleplayBotModule>> allModules = new HashSet<>();
	private static final Set<RoleplayBotModule> activeModules = new HashSet<>();

	public static void registerModule(Class<? extends RoleplayBotModule> module) {
		allModules.add(module);
	}

	public static void unloadModules() {
		for(RoleplayBotModule module : activeModules) {
			unloadModule(module);
		}
	}
	private static void unloadModule(RoleplayBotModule module) {
		Guild[] activeAt = module.getLoaded();
		for(Guild activeGuild : activeAt) {
			module.unload(activeGuild);
		}
	}

	public static void unloadModule(Class<? extends RoleplayBotModule> moduleClass, Guild guild) {
		RoleplayBotModule module = getLoadedModule(moduleClass);
		if(module != null) {
			module.unload(guild);
		}
	}
	public static boolean unloadModule(Guild guild, String moduleClass) {
		Class<? extends RoleplayBotModule> module = getModuleClassByName(moduleClass);
		if(module != null) {
			unloadModule(module, guild);
			return true;
		}

		return false;
	}

	public static Set<Class<? extends RoleplayBotModule>> getLoaded(Guild guild) {
		Set<Class<? extends RoleplayBotModule>> active = new HashSet<>();
		for(RoleplayBotModule module : activeModules) {
			if(module.isActive(guild)) {
				active.add(module.getClass());
			}
		}

		return active;
	}
	public static boolean isLoaded(String fullClassName, Guild guild) {
		Class<? extends RoleplayBotModule> moduleClass = getModuleClassByName(fullClassName);
		if(moduleClass != null) {
			RoleplayBotModule module = getLoadedModule(moduleClass);
			if(module != null) {
				return module.isActive(guild);
			}
		}

		return false;
	}
	public static RoleplayBotModule require(String module) {
		return getLoadedModule(getModuleClassByName(module));
	}

	public static void loadModules(Guild guild, String... moduleNames) {
		for(String moduleName : moduleNames) {
			loadModule(guild, moduleName);
		}
	}

	public static boolean loadModule(Guild guild, String name) {
		Class<? extends RoleplayBotModule> module = getModuleClassByName(name);
		if(module != null) {
			try {
				loadModuleFromClass(guild, module);
				return true;
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}

		System.out.printf("could not load module: %s%n", name);
		return false;
	}
	private static void loadModuleFromClass(Guild guild, @NotNull Class<? extends RoleplayBotModule> moduleClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		RoleplayBotModule module = getLoadedModule(moduleClass);
		if(module == null) {
			module = moduleClass.getDeclaredConstructor().newInstance();
			activeModules.add(module);
		}
		module.load(guild);
	}

	private static RoleplayBotModule getLoadedModule(Class<? extends RoleplayBotModule> moduleClass) {
		for(RoleplayBotModule activeModule: activeModules) {
			if(activeModule.getClass().equals(moduleClass)) {
				return activeModule;
			}
		}

		return null;
	}

	private static Class<? extends RoleplayBotModule> getModuleClassByName(String name) {
		for(Class<? extends RoleplayBotModule> module : allModules) {
			if(name.equals(module.getName())) {
				return module;
			}
		}

		return null;
	}

	private ModulesManager() {}
}
