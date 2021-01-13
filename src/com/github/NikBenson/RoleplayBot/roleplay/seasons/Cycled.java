package com.github.NikBenson.RoleplayBot.roleplay.seasons;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Cycled {
    protected final List<String> values;
    private int current = 0;

    public Cycled(JSONArray json) {
        values = new LinkedList<>();

        loadValues(json);
        next();
    }
    protected void loadValues(JSONArray json) {
        for (Object currentJson : json) {
            loadValue((JSONObject) currentJson);
        }
    }
    protected void loadValue(JSONObject json) {
        for (int j = 0; j < (long) json.getOrDefault("duration", 1l); j++) {
            addValue(json);
        }
    }
    protected void addValue(JSONObject json) {
        values.add((String) json.get("name"));
    }

    public void next() {
        if(++current >= values.size()) current = 0;
    }
    public String get() {
        return values.get(current);
    }
    protected void setCurrent(int i) {
        current = i;
        System.out.println(String.format("%d => %s", current, get()));
    }
}
