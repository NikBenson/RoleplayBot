package com.github.NikBenson.RoleplayBot.commands.context;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ServerContext extends GuildContext {

	public ServerContext(MessageReceivedEvent event) {
		super(event.getGuild());
		params.put("event", event);
	}
}
