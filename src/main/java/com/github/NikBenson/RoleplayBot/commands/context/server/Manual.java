package com.github.NikBenson.RoleplayBot.commands.context.server;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.ServerContext;
import com.github.NikBenson.RoleplayBot.serverCommands.ManualManager;

import java.util.Locale;

public class Manual extends Command<ServerContext> {

	@Override
	public Class<ServerContext> getContext() {
		return ServerContext.class;
	}

	@Override
	public String getRegex() {
		return "man( .*)?";
	}

	@Override
	public String execute(String command, ServerContext context) {
		String manual;
		if(command.equals("man")) {
			manual = ManualManager.getInstanceOrCreate().getManual("");
		} else {
			manual = ManualManager.getInstanceOrCreate().getManual(command.substring(4).trim().toLowerCase());
		}

		if(manual == null) {
			return "Manual not found!";
		}

		return manual;
	}
}
