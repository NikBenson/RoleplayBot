package com.github.NikBenson.RoleplayBot.roleplay;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChooseRandom {
	private double limit = 0;

	private String[] names;
	private double[] probabilities;

	public ChooseRandom(JSONArray json) {
		names = new String[json.size()];
		probabilities = new double[json.size()];

		for(int i = 0; i < json.size(); i++) {
			JSONObject obj = (JSONObject) json.get(i);

			names[i] = (String) obj.get("name");
			probabilities[i] = (double) obj.get("probability");

			limit += probabilities[i];
		}
	}

	public String get() {
		double random = Math.random() * limit;

		int i;
		for(i = 0; random >= 0; i++) {
			random -= probabilities[i];
		}

		return names[i - 1];
	}
}
