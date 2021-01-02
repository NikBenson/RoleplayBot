package com.github.NikBenson.RoleplayBot.commands.context.general;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.Context;
import com.github.NikBenson.RoleplayBot.roleplay.seasons.Season;

public class IngameLightLevel extends Command<Context> {

	@Override
	public Class<Context> getContext() {
		return Context.class;
	}

	@Override
	public String getRegex() {
		return "ingame lightLevel";
	}

	@Override
	public String execute(String command, Context context) {
		return Season.getCurrentLightLevel();
	}
}
