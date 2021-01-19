package com.github.NikBenson.RoleplayBot.commands.context.guild;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.GuildContext;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import net.dv8tion.jda.api.entities.Guild;

public class Save extends Command<GuildContext> {
	@Override
	public Class<GuildContext> getContext() {
		return GuildContext.class;
	}

	@Override
	public String getRegex() {
		return "save all";
	}

	@Override
	public String execute(String command, GuildContext context) {
		ConfigurationManager configurationManager = ConfigurationManager.getInstance();
		Guild guild = (Guild) context.getParams().get("guild");

		configurationManager.saveFrom(guild);

		return "finished!";
	}
}
