package com.github.NikBenson.RoleplayBot.commands.context.server;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.ServerContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;

public class Shutdown extends Command<ServerContext> {
	@Override
	public Class<ServerContext> getContext() {
		return ServerContext.class;
	}

	@Override
	public String getRegex() {
		return "shutdown";
	}

	@Override
	public String execute(String command, ServerContext context) {
		GenericEvent event = ((GenericEvent) context.getParams().get("event"));

		if(event instanceof MessageReceivedEvent && ((MessageReceivedEvent) event).getMember().hasPermission(Permission.ADMINISTRATOR)) {

			JDA jda = event.getJDA();

			jda.getEventManager().handle(new ShutdownEvent(jda, OffsetDateTime.now(), 0));

			jda.shutdown();

			System.exit(0);

			return "Bot stopped!";
		}

		return "Not permitted!";
	}
}
