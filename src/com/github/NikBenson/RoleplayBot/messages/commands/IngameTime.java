package com.github.NikBenson.RoleplayBot.messages.commands;

import com.github.NikBenson.RoleplayBot.roleplay.GameManager;

public class IngameTime extends Command {
	@Override
	public String getRegex() {
		return "ingame time( \"[\\. :-GyMwWDdFEuaHkKhmsSzZX]*\")?";
	}

	@Override
	public String execute(String command) {
		String pattern = "HH:mm";

		if(!command.equals("ingame time")) {
			pattern = command.substring(10, command.length() - 2);
		}

		return GameManager.getInstance().getTime(pattern);
	}
}
