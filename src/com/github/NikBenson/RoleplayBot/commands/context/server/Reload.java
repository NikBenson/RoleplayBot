package com.github.NikBenson.RoleplayBot.commands.context.server;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.ServerContext;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;

public class Reload extends Command<ServerContext> {
	@Override
	public Class<ServerContext> getContext() {
		return ServerContext.class;
	}

	@Override
	public String getRegex() {
		return "reload all";
	}

	@Override
	public String execute(String command, ServerContext context) {
		ConfigurationManager configurationManager = ConfigurationManager.getInstance();
		configurationManager.saveAll();
		configurationManager.loadAll();

		return "finished!";
	}
}
