package com.github.nikbenson.roleplaybot.commands.context;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class PrivateMessageContext extends Context {
	Map<String, Object> params = new HashMap<>();

	public PrivateMessageContext(PrivateMessageReceivedEvent event) {
		params.put("event", event);
	}

	@Override
	public Map<String, Object> getParams() {
		return params;
	}
}
