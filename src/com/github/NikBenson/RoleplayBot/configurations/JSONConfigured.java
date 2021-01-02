package com.github.NikBenson.RoleplayBot.configurations;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;

public interface JSONConfigured {
    JSONObject getJSON();
    File getConfigPath();
    void loadFromJSON(JSONObject json);
}
