package com.github.NikBenson.RoleplayBot.modules;

import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ModulesManager {
	private static final Set<Class<? extends RoleplayBotModule>> allModules = new HashSet<>();
	private static final Set<RoleplayBotModule> activeModules = new HashSet<>();

	public static void registerModule(Class<? extends RoleplayBotModule> module) {
		allModules.add(module);
		System.out.printf("registered %s, size is now %d%n", module.getName(), allModules.size());
	}

	public static Set<Class<? extends RoleplayBotModule>> getActive(Guild guild) {
		Set<Class<? extends RoleplayBotModule>> active = new HashSet<>();
		for(RoleplayBotModule module : activeModules) {
			if(module.isActive(guild)) {
				active.add(module.getClass());
			}
		}

		return active;
	}

	public static void activateModules(Guild guild, String... moduleNames) {
		for(String moduleName : moduleNames) {
			activateModule(guild, moduleName);
		}
	}

	public static boolean activateModule(Guild guild, String name) {
		Class<? extends RoleplayBotModule> module = getModuleClassByName(name);
		if(module != null) {
			try {
				activateModuleFromClass(guild, module);
				return true;
			} catch (Exception e) {}
		}

		System.out.printf("could not activate module: %s%n", name);
		return false;
	}
	private static void activateModuleFromClass(Guild guild, @NotNull Class<? extends RoleplayBotModule> moduleClass) throws IllegalAccessException, InstantiationException {
		RoleplayBotModule module = getActiveModule(moduleClass);
		if(module == null) {
			module = moduleClass.newInstance();
			activeModules.add(module);
		}
		module.load(guild);
	}

	private static RoleplayBotModule getActiveModule(Class<? extends RoleplayBotModule> moduleClass) {
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
