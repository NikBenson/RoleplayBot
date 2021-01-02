package com.github.NikBenson.RoleplayBot.roleplay.seasons;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Season {
	private static Season[] all;
	private static int current;

	private static Season getCurrent() {
		return all[current];
	}

	public static void update() {
		getCurrent().next();
	}

	public static String getCurrentSeason() {
		return getCurrent().getName();
	}

	public static String getCurrentTemperature() {
		return getCurrent().getTemperature();
	}

	public static String getCurrentLightLevel() {
		return getCurrent().getLightLevel();
	}

	public static String getCurrentWeather() {
		return getCurrent().getWeather();
	}

	public static void createSeasons(JSONArray json, long passedUpdates) {
		all = new Season[json.size()];

		for(int i = 0; i < all.length; i++) {
			all[i] = new Season((JSONObject) json.get(i));
		}

		current = 0;

		getCurrent().next(passedUpdates);
	}

	private final String name;

	private final long length;
	private long time = 1;

	private ChooseRandom weatherGenerator;
	private ChooseRandom temperatureGenerator;
	private List<String> lightLevels = new LinkedList<>();

	private int currentLightLevel = 0;
	private String currentWeather;
	private String currentTemperature;

	private Season(JSONObject json) {
		name = (String) json.get("name");
		length = (long) json.get("length");

		weatherGenerator = new ChooseRandom((JSONArray) json.get("weathers"));
		temperatureGenerator = new ChooseRandom((JSONArray) json.get("temperatures"));
		JSONArray lightLevelsJSON = (JSONArray) json.get("lightLevels");
		for(int i = 0; i < lightLevelsJSON.size(); i++) {
			JSONObject lightLevel = (JSONObject) lightLevelsJSON.get(i);

			String name = (String) lightLevel.get("name");

			for(int j = 0; j < (long)lightLevel.get("duration"); j++) {
				lightLevels.add(name);
			}
		}
	}

	private void next() {
		next(1l);
	}
	private void next(long delta) {
		time += delta;
		if(time > length) {
			time = 1;

			if(++current >= all.length) {
				current = 0;
			}
			getCurrent().next(delta - length);
		} else {
			generateValues();
		}
	}
	private void generateValues() {
		if(++currentLightLevel >= lightLevels.size()) {
			currentLightLevel = 0;
		}

		currentWeather = weatherGenerator.get();
		currentTemperature = temperatureGenerator.get();
	}

	private String getName() {
		return name;
	}

	private String getWeather() {
		return currentWeather;
	}

	private String getTemperature() {
		return currentTemperature;
	}

	private String getLightLevel() {
		return lightLevels.get(currentLightLevel);
	}
}
