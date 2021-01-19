package com.github.NikBenson.RoleplayBot.commands.context.cli;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.CLIContext;
import com.github.NikBenson.RoleplayBot.modules.ModulesManager;

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
		ModulesManager.reload();

		return "reloaded modules!";
	}
}
