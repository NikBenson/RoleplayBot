package com.github.nikbenson.roleplaybot.commands.context.cli;

import com.github.nikbenson.roleplaybot.Bot;
import com.github.nikbenson.roleplaybot.commands.Command;
import com.github.nikbenson.roleplaybot.commands.context.CLIContext;
import com.github.nikbenson.roleplaybot.configurations.ConfigurationManager;
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