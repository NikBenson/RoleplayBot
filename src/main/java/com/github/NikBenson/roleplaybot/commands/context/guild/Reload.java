package com.github.nikbenson.roleplaybot.commands.context.guild;

import com.github.nikbenson.roleplaybot.commands.Command;
import com.github.nikbenson.roleplaybot.commands.context.GuildContext;
import com.github.nikbenson.roleplaybot.configurations.ConfigurationManager;
import net.dv8tion.jda.api.entities.Guild;

public class Reload extends Command<GuildContext> {
	@Override
	public Class<GuildContext> getContext() {
		return GuildContext.class;
	}

	@Override
	public String getRegex() {
		return "reload all";
	}

	@Override
	public String execute(String command, GuildContext context) {
		ConfigurationManager configurationManager = ConfigurationManager.getInstance();
		Guild guild = (Guild) context.getParams().get("guild");

		configurationManager.saveFrom(guild);
		configurationManager.loadFrom(guild);

		return "finished!";
	}
}
