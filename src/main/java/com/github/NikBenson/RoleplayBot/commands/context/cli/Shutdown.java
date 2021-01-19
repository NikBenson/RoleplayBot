package com.github.NikBenson.RoleplayBot.commands.context.cli;

import com.github.NikBenson.RoleplayBot.Bot;
import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.CLIContext;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import net.dv8tion.jda.api.JDA;

public class Shutdown extends Command<CLIContext> {
	@Override
	public Class<CLIContext> getContext() {
		return CLIContext.class;
	}

	@Override
	public String getRegex() {
		return "shutdown";
	}

	@Override
	public String execute(String command, CLIContext context) {
		ConfigurationManager.getInstance().saveAll();

		JDA jda = Bot.getJDA();

		jda.shutdown();

		System.exit(0);

		return "Bot stopped!";
	}
}