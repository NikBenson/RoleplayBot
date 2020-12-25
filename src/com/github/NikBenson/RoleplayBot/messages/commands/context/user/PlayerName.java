package com.github.NikBenson.RoleplayBot.messages.commands.context.user;

import com.github.NikBenson.RoleplayBot.messages.commands.Command;
import net.dv8tion.jda.api.entities.User;

public class PlayerName extends Command<UserContext> {
	@Override
	public String getRegex() {
		return "player name";
	}

	@Override
	public String execute(String command, UserContext context) {
		return ((User)context.getParams().get("user")).getName();
	}
}
