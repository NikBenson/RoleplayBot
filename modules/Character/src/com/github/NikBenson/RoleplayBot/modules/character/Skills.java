package com.github.NikBenson.RoleplayBot.modules.character;

import org.json.simple.JSONObject;

import java.util.Map;

public class Skills {
	private Map<String, Long> skills = new JSONObject();
	private long openPoints = 0;

	public Skills(JSONObject json) {
		skills = json;
	}

	public Skills() {
		for (String skill : SheetBlueprint.getInstanceOrCreate().getAllSkills()) {
			skills.put(skill, 0l);
		}
	}

	public long get(String skill) {
		return skills.getOrDefault(skill, 0l);
	}

	public Map<String, Long> getSkills() {
		return skills;
	}

	public String add(String skill) {
		if (openPoints > 0) return "no skill points!";
		if (SheetBlueprint.getInstanceOrCreate().getAllSkills().contains(skill)) return "not a skill!";

		Long before = skills.get(skill);

		skills.replace(skill, before + 1);
		openPoints--;

		return String.format("Successfully skilled %s! You now have %d open points left.", skill, openPoints);
	}

	public long addPoint() {
		return ++openPoints;
	}
	public long getOpenPoints() {
		return openPoints;
	}
}
