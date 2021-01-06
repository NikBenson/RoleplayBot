package com.github.NikBenson.RoleplayBot.configurations;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;

public interface JSONConfigured {
    JSONObject getJSON();
    @NotNull File getConfigPath();
    void loadFromJSON(JSONObject json);
}
