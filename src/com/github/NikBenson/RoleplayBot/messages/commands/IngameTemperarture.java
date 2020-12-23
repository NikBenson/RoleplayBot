package com.github.NikBenson.RoleplayBot.messages.commands;

import com.github.NikBenson.RoleplayBot.roleplay.Season;

public class IngameTemperarture extends Command {
	@Override
	public String getRegex() {
		return "ingame temperature";
	}

	@Override
	public String execute(String command) {
		return Season.getCurrentTemperature();
	}
}
