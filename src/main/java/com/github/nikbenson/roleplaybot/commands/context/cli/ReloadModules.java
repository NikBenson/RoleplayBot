package com.github.nikbenson.roleplaybot.commands.context.cli;

import com.github.nikbenson.roleplaybot.commands.Command;
import com.github.nikbenson.roleplaybot.commands.context.CLIContext;
import com.github.nikbenson.roleplaybot.configurations.ConfigurationManager;

public class ReloadModules extends Command<CLIContext> {
	@Override
	public Class<CLIContext> getContext() {
		return CLIContext.class;
	}

	@Override
	public String getRegex() {
		return "reload modules";
	}

	@Override
	public String execute(String command, CLIContext context) {
		ConfigurationManager configurationManager = ConfigurationManager.getInstance();
		configurationManager.saveAll();
		configurationManager.loadAll();

		return "reloaded modules!";
	}
}
