package com.github.NikBenson.RoleplayBot.commands.context.guild;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.GuildMessageContext;
import com.github.NikBenson.RoleplayBot.modules.ModuleConfig;
import net.dv8tion.jda.api.entities.Guild;

public class ModuleLoad extends Command<GuildMessageContext> {
	@Override
	public Class<GuildMessageContext> getContext() {
		return GuildMessageContext.class;
	}

	@Override
	public String getRegex() {
		return "module load [a-zA-Z.]+";
	}

	@Override
	public String execute(String command, GuildMessageContext context) {
		Guild guild = (Guild) context.getParams().get("guild");
		String module = command.substring(12);

		return ModuleConfig.getOrCreate(guild).loadModule(module)? "Module loaded" : "Failed loading module";
	}
}
