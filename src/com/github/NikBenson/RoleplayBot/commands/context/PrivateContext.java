package com.github.NikBenson.RoleplayBot.commands.context;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class PrivateContext extends Context {
	Map<String, Object> params = new HashMap<>();

	public PrivateContext(PrivateMessageReceivedEvent event) {
		params.put("event", event);
	}

	@Override
	public Map<String, Object> getParams() {
		return params;
	}
}
