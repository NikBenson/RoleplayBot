package com.github.NikBenson.RoleplayBot.modules;

import com.github.NikBenson.RoleplayBot.Bot;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleConfig implements JSONConfigured {
	private static final Map<Guild, ModuleConfig> all = new HashMap<>();

	public static void loadModules() {
		File guildsDirectory = new File(ConfigurationManager.getInstance().getConfigurationRootPath(), ConfigurationPaths.GUILDS_DIRECTORY);
		File[] guilds = guildsDirectory.listFiles();
		if(guilds != null) {
			for (File guildDirectory : guilds) {
				try {
					String guildId = guildDirectory.getName();
					Guild guild = Bot.getJDA().getGuildById(guildId);

					new ModuleConfig(guild);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static ModuleConfig getOrCreate(Guild guild) {
		if(!all.containsKey(guild)) {
			all.put(guild, new ModuleConfig(guild));
		}

		return all.get(guild);
	}

	private final Guild GUILD;
	private List<String> loadedModules = new JSONArray();

	private ModuleConfig(Guild guild) {
		GUILD = guild;

		ConfigurationManager configurationManager = ConfigurationManager.getInstance();
		configurationManager.registerConfiguration(this);
		try {
			configurationManager.load(this);
		} catch (Exception ignored) {}

		all.put(GUILD, this);
	}

	public boolean loadModule(String module) {
		if(loadedModules.contains(module)) return true;

		if(ModulesManager.loadModule(GUILD, module)) {
			loadedModules.add(module);
			return true;
		}
		return false;
	}
	public boolean unloadModule(String module) {
		if(ModulesManager.unloadModule(GUILD, module)) {
			loadedModules.remove(module);
			return true;
		}
		return false;
	}

	@Override
	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		json.put("loaded", loadedModules);
		return json;
	}

	@Override
	public @NotNull File getConfigPath() {
		return new File(ConfigurationManager.getInstance().getConfigurationRootPath(GUILD), ConfigurationPaths.MODULES_FILE);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		for(String module : loadedModules) {
			unloadModule(module);
		}

		JSONArray modulesInConfig = (JSONArray) json.getOrDefault("loaded", new JSONArray());

		for (Object o : modulesInConfig) {
			String moduleInConfig = (String) o;
			loadModule(moduleInConfig);
		}
	}
}
