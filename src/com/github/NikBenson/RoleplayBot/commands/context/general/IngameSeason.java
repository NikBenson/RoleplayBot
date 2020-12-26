package com.github.NikBenson.RoleplayBot.commands.context.general;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.GeneralContext;
import com.github.NikBenson.RoleplayBot.roleplay.Season;

public class IngameSeason extends Command<GeneralContext> {
	@Override
	public String getRegex() {
		return "ingame season";
	}

	@Override
	public String execute(String command, GeneralContext context) {
		return Season.getCurrentSeason();
	}
}
