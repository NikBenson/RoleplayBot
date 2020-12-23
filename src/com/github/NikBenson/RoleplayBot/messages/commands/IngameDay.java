package com.github.NikBenson.RoleplayBot.messages.commands;

import com.github.NikBenson.RoleplayBot.roleplay.GameManager;

public class IngameDay extends Command {
	@Override
	public String getRegex() {
		return "ingame day";
	}

	@Override
	public String execute(String command) {
		return String.valueOf(GameManager.getInstance().getDay());
	}
}
