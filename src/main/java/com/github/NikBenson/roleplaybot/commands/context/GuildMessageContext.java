package com.github.nikbenson.roleplaybot.commands.context;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GuildMessageContext extends GuildContext {

	public GuildMessageContext(GuildMessageReceivedEvent event) {
		super(event.getGuild());
		params.put("event", event);
	}
}
