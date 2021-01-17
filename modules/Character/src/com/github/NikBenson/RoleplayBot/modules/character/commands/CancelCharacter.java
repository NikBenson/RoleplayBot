package com.github.NikBenson.RoleplayBot.modules.character.commands;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.PrivateContext;
import com.github.NikBenson.RoleplayBot.users.PlayerManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class CancelCharacter extends Command<PrivateContext> {
	@Override
	public Class<PrivateContext> getContext() {
		return PrivateContext.class;
	}

	@Override
	public String getRegex() {
		return "cancel character";
	}

	@Override
	public String execute(String command, PrivateContext context) {
		User user = ((PrivateMessageReceivedEvent) context.getParams().get("event")).getAuthor();

		PlayerManager.getInstance().getPlayerOrCreate(user).cancelCharacterCreation();

		return "Canceled!";
	}
}
