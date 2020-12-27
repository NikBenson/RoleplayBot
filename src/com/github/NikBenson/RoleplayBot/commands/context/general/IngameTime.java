package com.github.NikBenson.RoleplayBot.commands.context.general;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.GeneralContext;
import com.github.NikBenson.RoleplayBot.roleplay.GameManager;

public class IngameTime extends Command<GeneralContext> {
	@Override
	public String getRegex() {
		return "ingame time( \"[\\. :-GyMwWDdFEuaHkKhmsSzZX]*\")?";
	}

	@Override
	public String execute(String command, GeneralContext context) {
		String pattern = "HH:mm";

		if(!command.equals("ingame time")) {
			pattern = command.substring(13, command.length() - 2);
		}

		return GameManager.getInstance().getTime(pattern);
	}
}
