package com.github.NikBenson.RoleplayBot.commands.context.server;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.ServerContext;
import com.github.NikBenson.RoleplayBot.roleplay.StorageManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Map;

public class Storage extends Command<ServerContext> {

	@Override
	public Class<ServerContext> getContext() {
		return ServerContext.class;
	}

	@Override
	public String getRegex() {
		return "storage( ((put [a-zA-Z]*)|(take [a-zA-Z]*)))?";
	}

	@Override
	public String execute(String command, ServerContext context) {
		TextChannel channel = ((MessageReceivedEvent) context.getParams().get("event")).getTextChannel();

		if(command.equals("storage")) {
			return listContent(channel);
		} else if(command.startsWith("storage put")) {
			String item = command.substring(11).trim().toLowerCase();
			return String.format("**%s**: %d", item, StorageManager.addTo(channel , item));
		} else if(command.startsWith("storage take")) {
			String item = command.substring(13).trim().toLowerCase();
			if(StorageManager.takeFrom(channel, item)) {
				return String.format("**%s**: %d", item, StorageManager.getStorageFrom(channel , item));
			} else {
				return String.format("**%s** not found.", item);
			}
		}
		return "Syntax Error!";
	}

	private String listContent(TextChannel channel) {
		Map<String, Long> storage = StorageManager.getStorageFrom(channel);
		String list = "";

		for(String item : storage.keySet()) {
			long count = storage.get(item);

			list += String.format("**%s**: %d\n", item, count);
		}

		if(list.length() > 0) {
			list.substring(0, list.length() - 2);
		} else {
			list = "Empty";
		}

		return list;
	}
}
