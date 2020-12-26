package com.github.NikBenson.RoleplayBot.commands.context.user;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.UserContext;
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
