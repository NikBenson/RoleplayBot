package com.github.NikBenson.RoleplayBot.commands.context.general;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.Context;
import com.github.NikBenson.RoleplayBot.roleplay.GameManager;

public class IngameDay extends Command<Context> {
	@Override
	public Class<Context> getContext() {
		return Context.class;
	}

	@Override
	public String getRegex() {
		return "ingame day";
	}

	@Override
	public String execute(String command, Context context) {
		return String.valueOf(GameManager.getInstanceOrCreate().getDay());
	}
}
