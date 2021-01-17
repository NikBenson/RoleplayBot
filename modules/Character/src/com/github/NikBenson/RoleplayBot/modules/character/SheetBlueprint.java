package com.github.NikBenson.RoleplayBot.modules.character;

import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import com.github.NikBenson.RoleplayBot.modules.ModulesManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SheetBlueprint implements JSONConfigured {
	private static SheetBlueprint instance;

	public static SheetBlueprint getInstanceOrCreate() {
		if(instance == null) {
			instance = new SheetBlueprint();
		}

		return instance;
	}

	private List<String> sheetAttributes = new LinkedList<>();
	private List<String> sheetQuestions = new LinkedList<>();
	private List<String> allSkills = new LinkedList<>();

	private SheetBlueprint() {
		ModulesManager.registerModule("charactercreation", this);
	}

	public String getSheetAttribute(int i) {
		try {
			return sheetAttributes.get(i);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}

	public String getSheetQuestion(int i) {
		try {
			return sheetQuestions.get(i);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}

	public List<String> getAllSkills() {
		return allSkills;
	}

	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public File getConfigPath() {
		return new File(ConfigurationManager.getInstance().getConfigurationRootPath(), ConfigurationPaths.CHARACTER_GENERATION_FILE);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		JSONArray sheet = (JSONArray) json.get("sheet");

		sheetAttributes.clear();
		sheetQuestions.clear();

		for (int i = 0; i < sheet.size(); i++) {
			JSONObject current = (JSONObject) sheet.get(i);

			sheetAttributes.add((String) current.get("name"));
			sheetQuestions.add((String) current.get("question"));
		}

		allSkills = (JSONArray) json.get("skills");
	}


}
