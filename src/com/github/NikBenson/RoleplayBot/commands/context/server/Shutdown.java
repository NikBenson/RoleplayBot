package com.github.NikBenson.RoleplayBot.commands.context.server;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.ServerContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;

import java.time.OffsetDateTime;

public class Shutdown extends Command<ServerContext> {
	@Override
	public String getRegex() {
		return "shutdown";
	}

	@Override
	public String execute(String command, ServerContext context) {
		JDA jda = ((GenericEvent) context.getParams().get("event")).getJDA();

		jda.getEventManager().handle(new ShutdownEvent(jda, OffsetDateTime.now(), 0));

		jda.shutdown();

		System.exit(0);

		return "Bot stopped!";
	}
}
