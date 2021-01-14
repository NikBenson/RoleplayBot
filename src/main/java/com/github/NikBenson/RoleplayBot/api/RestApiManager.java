package com.github.NikBenson.RoleplayBot.api;

import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;

import java.io.File;

public class RestApiManager implements JSONConfigured {
	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public @NotNull File getConfigPath() {
		return null;
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		SpringApplication app = new SpringApplication(RestApi.class);
		app.run();
	}
}
