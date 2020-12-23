package com.github.NikBenson.RoleplayBot.messages.commands;

import com.github.NikBenson.RoleplayBot.roleplay.Season;

public class IngameSeason extends Command {
	@Override
	public String getRegex() {
		return "ingame season";
	}

	@Override
	public String execute(String command) {
		return Season.getCurrentSeason();
	}
}
