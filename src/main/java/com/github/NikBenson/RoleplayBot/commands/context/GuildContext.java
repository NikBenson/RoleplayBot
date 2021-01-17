package com.github.NikBenson.RoleplayBot.commands.context;

import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class GuildContext extends Context {
	protected Map<String, Object> params = new HashMap<>();

	public GuildContext(Guild guild) {
		params.put("guild", guild);
	}

	@Override
	public Map<String, Object> getParams() {
		return params;
	}
}
