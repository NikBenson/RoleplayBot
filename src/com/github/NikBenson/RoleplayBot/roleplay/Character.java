package com.github.NikBenson.RoleplayBot.roleplay;

import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Character {
	private static List<String> sheetAttributes = new LinkedList<>();
	private static List<String> sheetQuestions = new LinkedList<>();

	public static void setSheetAttributes(List<String> attributes) {
		sheetAttributes = attributes;
	}
	public static String getSheetAttribute(int i) {
		try {
			return sheetAttributes.get(i);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}

	public static void setSheetQuestions(List<String> questions) {
		sheetQuestions = questions;
	}
	public static String getSheetQuestion(int i) {
		try {
			return sheetQuestions.get(i);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}

	private Skills skills;
	private Team team;
	private Map<String, String> sheet;

	public Character(JSONObject json) {
		skills = new Skills((JSONObject) json.get("skills"));
		sheet = (Map<String, String>) json.get("sheet");
		team = Team.findTeam((String) json.get("team"));
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
