package com.github.NikBenson.RoleplayBot.roleplay.seasons;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Season {
	private static Season[] all;
	private static int current;

	public static Season getCurrent() {
		return all[current];
	}

	public static void update() {
		getCurrent().next();
	}

	public static void createSeasons(JSONArray json, long passedUpdates) {
		all = new Season[json.size()];

		for(int i = 0; i < all.length; i++) {
			all[i] = new Season((JSONObject) json.get(i));
		}

		current = 0;

		getCurrent().next(passedUpdates);
	}

	private final long length;
	private long time = 1;

	private Map<String, String> constants = new HashMap<>();
	private Map<String, Cycled> cyclical = new HashMap<>();
	private Map<String, ChooseRandom> randoms = new HashMap<>();

	private Season(JSONObject json) {
		length = (long) json.get("length");

		JSONArray values = (JSONArray) json.get("values");

		for(Object o : values) {
			JSONObject value = (JSONObject) o;
			String type = (String) value.getOrDefault("type", "constant");
			switch (type) {
				case "constant": addConstant(value); break;
				case "cyclic": addCyclic(value); break;
				case "probability": addProbability(value);
			}
		}
	}
	private void addConstant(JSONObject json) {
		String name = (String) json.get("name");
		String value = (String) json.get("value");
		constants.put(name, value);
	}
	private void addCyclic(JSONObject json) {
		String name = (String) json.get("name");
		JSONArray values = (JSONArray) json.get("values");
		cyclical.put(name, new Cycled(values));
	}
	private void addProbability(JSONObject json) {
		String name = (String) json.get("name");
		JSONArray values = (JSONArray) json.get("values");
		randoms.put(name, new ChooseRandom(values));
	}

	private void next() {
		next(1l);
	}
	private void next(long delta) {
		time++;
		cycle();

		if(time > length) {
			time = 1;

			if(++current >= all.length) {
				current = 0;
			}
		}

		if(delta > 0) {
			getCurrent().next(delta - 1);
		}
	}
	private void cycle() {
	    for(Cycled cycled : cyclical.values()) {
	    	cycled.next();
		}
	    generateValues();
	}
	private void generateValues() {
		for (ChooseRandom random : randoms.values()) {
			random.next();
		}
	}

	public String get(String name) {
		if(constants.containsKey(name)) {
			return constants.get(name);
		} else if(cyclical.containsKey(name)) {
			return cyclical.get(name).get();
		} else if(randoms.containsKey(name)){
			return randoms.get(name).get();
		} else {
			return null;
		}
	}
}
