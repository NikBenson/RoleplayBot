package com.github.NikBenson.RoleplayBot.commands.context.general;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.GeneralContext;
import com.github.NikBenson.RoleplayBot.roleplay.GameManager;

public class IngameDay extends Command<GeneralContext> {
	@Override
	public String getRegex() {
		return "ingame day";
	}

	@Override
	public String execute(String command, GeneralContext context) {
		return String.valueOf(GameManager.getInstance().getDay());
	}
}
