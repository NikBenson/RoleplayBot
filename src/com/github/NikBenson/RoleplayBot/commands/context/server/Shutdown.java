package com.github.NikBenson.RoleplayBot.commands.context.server;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.ServerContext;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
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
			ConfigurationManager.getInstance().saveAll();

			JDA jda = event.getJDA();

			jda.shutdown();

			System.exit(0);

			return "Bot stopped!";
		}

		return "Not permitted!";
	}
}
