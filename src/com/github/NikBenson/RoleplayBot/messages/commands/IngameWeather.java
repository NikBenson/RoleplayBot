package com.github.NikBenson.RoleplayBot.messages.commands;

import com.github.NikBenson.RoleplayBot.roleplay.Season;

public class IngameWeather extends Command {
	@Override
	public String getRegex() {
		return "ingame weather";
	}

	@Override
	public String execute(String command) {
		return Season.getCurrentWeather();
	}
}
