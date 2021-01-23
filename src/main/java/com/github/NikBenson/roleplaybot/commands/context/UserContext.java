package com.github.nikbenson.roleplaybot.commands.context;

import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class UserContext extends Context {
	private Map<String, Object> params = new HashMap<>();

	public UserContext(User user) {
		params.put("user", user);
	}

	@Override
	public Map<String, Object> getParams() {
		return params;
	}
}
