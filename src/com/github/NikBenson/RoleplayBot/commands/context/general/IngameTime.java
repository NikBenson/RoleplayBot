package com.github.NikBenson.RoleplayBot.commands.context.general;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.Context;
import com.github.NikBenson.RoleplayBot.roleplay.GameManager;

public class IngameTime extends Command<Context> {
	@Override
	public Class<Context> getContext() {
		return Context.class;
	}

	@Override
	public String getRegex() {
		return "ingame time( \"[\\. :-GyMwWDdFEuaHkKhmsSzZX]*\")?";
	}

	@Override
	public String execute(String command, Context context) {
		String pattern = "HH:mm";

		if(!command.equals("ingame time")) {
			pattern = command.substring(13, command.length() - 2);
		}

		return GameManager.getInstance().getTime(pattern);
	}
}
