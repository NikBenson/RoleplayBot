package com.github.NikBenson.RoleplayBot.roleplay;

import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Skills {
	private static List<String> all = new LinkedList<>();

	public static List<String> getAll() {
		return all;
	}

	private Map<String, Long> skills = new JSONObject();
	private int openPoints = 0;

	public Skills(JSONObject json) {
		skills = json;
	}

	public Skills() {
		for(String skill : all) {
			skills.put(skill, 0l);
		}
	}

	public long get(String skill) {
		return skills.getOrDefault(skill, 0l);
	}

	public Map<String, Long> getSkills() {
		return skills;
	}

	public boolean add(String skill) {
		if(openPoints > 0) {
			Long before = skills.get(skill);

			skills.replace(skill, before + 1);
			openPoints--;

			return true;
		}

		return false;
	}

	public long addPoint() {
		return ++openPoints;
	}
}
