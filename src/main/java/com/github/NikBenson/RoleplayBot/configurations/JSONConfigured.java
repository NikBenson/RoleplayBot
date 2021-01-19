package com.github.NikBenson.RoleplayBot.configurations;

import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.io.File;

public interface JSONConfigured {
    JSONObject getJSON();
    @NotNull File getConfigPath();
    void loadFromJSON(JSONObject json);

	default Guild getGuild() {
		return null;
	}
}
