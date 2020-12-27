package com.github.NikBenson.RoleplayBot.commands.context;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class ServerContext extends GeneralContext {
	Map<String, Object> params = new HashMap<>();

	public ServerContext(MessageReceivedEvent event) {
		params.put("event", event);
	}

	@Override
	public Map<String, Object> getParams() {
		return params;
	}
}
