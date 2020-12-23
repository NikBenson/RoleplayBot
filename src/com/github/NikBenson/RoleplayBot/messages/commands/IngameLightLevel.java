package com.github.NikBenson.RoleplayBot.messages.commands;

import com.github.NikBenson.RoleplayBot.roleplay.Season;

public class IngameLightLevel extends Command {

	@Override
	public String getRegex() {
		return "ingame lightLevel";
	}

	@Override
	public String execute(String command) {
		return Season.getCurrentLightLevel();
	}
}
