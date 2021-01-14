package com.github.NikBenson.RoleplayBot.roleplay.seasons;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ChooseRandom extends Cycled {
	private double limit;

	private List<Double> probabilities;


	public ChooseRandom(JSONArray json) {
		super(json);
	}

	@Override
	protected void loadValues(JSONArray json) {
		probabilities = new LinkedList<>();
		super.loadValues(json);
	}

	@Override
	protected void addValue(JSONObject json) {
		super.addValue(json);

		double probability = (double) json.getOrDefault("probability", 1d);
		probabilities.add(probability);
		limit += probability;
	}

	@Override
	public void next() {
		double random = Math.random() * limit;

		int i;
		for (i = 0; random >= 0 && i < probabilities.size(); i++) {
			random -= probabilities.get(i);
		}

		setCurrent(i - 1);
	}
}
