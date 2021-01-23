package com.github.nikbenson.roleplaybot.commands.context.guild;

import com.github.nikbenson.roleplaybot.commands.Command;
import com.github.nikbenson.roleplaybot.commands.context.GuildMessageContext;
import com.github.nikbenson.roleplaybot.modules.ModuleConfig;
import net.dv8tion.jda.api.entities.Guild;

public class ModuleUnload extends Command<GuildMessageContext> {
	@Override
	public Class<GuildMessageContext> getContext() {
		return GuildMessageContext.class;
	}

	@Override
	public String getRegex() {
		return "module unload [a-zA-Z.]+";
	}

	@Override
	public String execute(String command, GuildMessageContext context) {
		Guild guild = (Guild) context.getParams().get("guild");
		String module = command.substring(14);

		return ModuleConfig.getOrCreate(guild).unloadModule(module)? "Module unloaded" : "Failed unloading module";
	}
}
