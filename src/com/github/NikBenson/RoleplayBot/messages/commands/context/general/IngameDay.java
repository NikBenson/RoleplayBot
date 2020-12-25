package com.github.NikBenson.RoleplayBot.messages.commands.context.general;

import com.github.NikBenson.RoleplayBot.messages.commands.Command;
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
