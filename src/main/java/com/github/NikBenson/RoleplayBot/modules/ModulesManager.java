package com.github.NikBenson.RoleplayBot.modules;

import com.github.NikBenson.RoleplayBot.Bot;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager.readJSONFromFile;

public class ModulesManager {
	private static final Set<Class<? extends RoleplayBotModule>> allModules = new HashSet<>();
	private static final Set<RoleplayBotModule> activeModules = new HashSet<>();

	public static void registerModule(Class<? extends RoleplayBotModule> module) {
		if(allModules.contains(module)) {
			allModules.remove(module);
		}
		allModules.add(module);
	}

	public static void reload() {
		for(RoleplayBotModule module : activeModules) {
			unloadModule(module);
		}

		loadModules();
	}
	private static void unloadModule(RoleplayBotModule module) {
		Guild[] activeAt = module.getLoaded();
		for(Guild activeGuild : activeAt) {
			module.unload(activeGuild);
		}
	}
	private static void loadModules() {
		File guildsDirectory = new File(ConfigurationManager.getInstance().getConfigurationRootPath(), ConfigurationPaths.GUILDS_DIRECTORY);
		File[] guilds = guildsDirectory.listFiles();
		if(guilds != null) {
			for (File guildDirectory : guilds) {
				try {
					String guildId = guildDirectory.getName();
					Guild guild = Bot.getJDA().getGuildById(guildId);
					JSONArray modules = (JSONArray) readJSONFromFile(new File(guildDirectory, ConfigurationPaths.MODULES_FILE));

					ModulesManager.activateModules(guild, (String[]) modules.toArray(new String[0]));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
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
			} catch (Exception ignored) {}
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
