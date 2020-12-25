package com.github.NikBenson.RoleplayBot.messages.commands.context.general;

import com.github.NikBenson.RoleplayBot.messages.commands.Command;
import com.github.NikBenson.RoleplayBot.roleplay.Season;

public class IngameWeather extends Command<GeneralContext> {
	@Override
	public String getRegex() {
		return "ingame weather";
	}

	@Override
	public String execute(String command, GeneralContext context) {
		return Season.getCurrentWeather();
	}
}
