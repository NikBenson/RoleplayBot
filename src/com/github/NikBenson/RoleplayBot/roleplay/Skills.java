package com.github.NikBenson.RoleplayBot.roleplay;

import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Skills {
	private static List<String> allSkills = new LinkedList<>();

	public static List<String> getAllSkills() {
		return allSkills;
	}

	public static void setAllSkills(List<String> allSkills) {
		Skills.allSkills = allSkills;
	}

	private Map<String, Long> skills = new JSONObject();
	private long openPoints = 0;

	public Skills(JSONObject json) {
		skills = json;
	}

	public Skills() {
		for (String skill : allSkills) {
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
		if (allSkills.contains(skill)) return "not a skill!";

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
