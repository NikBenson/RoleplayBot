package com.github.NikBenson.RoleplayBot.serverCommands;

import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ManualManager implements JSONConfigured {
	private static ManualManager instance;

	public static ManualManager getInstanceOrCreate() {
		if(instance == null) {
			instance = new ManualManager();
		}

		return instance;
	}

	private Map<String, String> manuals = new HashMap<>();

	private ManualManager() {
		ConfigurationManager configurationManager = ConfigurationManager.getInstance();
		configurationManager.registerConfiguration(this);
		try {
			configurationManager.load(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getManual(String query) {
		return manuals.get(query);
	}

	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public File getConfigPath() {
		return new File(ConfigurationManager.getInstance().getConfigurationRootPath(), ConfigurationPaths.MANUALS_FILE);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		manuals = json;
	}
}
