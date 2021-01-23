package com.github.nikbenson.roleplaybot.commands.context.user;

import com.github.nikbenson.roleplaybot.commands.Command;
import com.github.nikbenson.roleplaybot.commands.context.UserContext;
import net.dv8tion.jda.api.entities.User;

public class PlayerName extends Command<UserContext> {
	@Override
	public Class<UserContext> getContext() {
		return UserContext.class;
	}

	@Override
	public String getRegex() {
		return "player name";
	}

	@Override
	public String execute(String command, UserContext context) {
		return ((User)context.getParams().get("user")).getName();
	}
}
