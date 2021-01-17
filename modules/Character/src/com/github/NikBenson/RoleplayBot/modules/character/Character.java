package com.github.NikBenson.RoleplayBot.modules.character;

import org.json.simple.JSONObject;

import java.util.Map;

public class Character {
	private Skills skills;
	private Team team;
	private Map<String, String> sheet;

	public Character(JSONObject json) {
		skills = new Skills((JSONObject) json.get("skills"));
		sheet = (Map<String, String>) json.get("sheet");
		team = TeamsManager.getInstance().findTeam((String) json.get("team"));
	}

	public Character(Map<String, String> sheet, Team team) {
		skills = new Skills();
		this.sheet = sheet;
		this.team = team;

		team.registerCharacter(this);
	}

	public Skills getSkills() {
		return skills;
	}

	public Map<String, String> getSheet() {
		return sheet;
	}
	public String getAttribute(String name) {
		return sheet.get(name);
	}

	public JSONObject getJson() {
		JSONObject json = new JSONObject();

		json.put("skills", skills.getSkills());
		json.put("sheet", sheet);
		json.put("team", team.getName());

		return json;
	}
}
