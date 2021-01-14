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
		return "storage( ((put)|(take)) .*)?";
	}

	@Override
	public String execute(String command, ServerContext context) {
		TextChannel channel = ((MessageReceivedEvent) context.getParams().get("event")).getTextChannel();

		if(command.equals("storage")) {
			return listContent(channel);
		} else if(command.startsWith("storage put")) {
			String args = command.substring(12).trim().toLowerCase();
			String item;

			int lastIndex = args.lastIndexOf(' ');
			int count = 1;
			try {
				count = Integer.parseInt(args.substring(lastIndex + 1));
				item = args.substring(0, lastIndex);
			} catch(NumberFormatException exception) {
				item = args;
			}

			return String.format("**%s**: %d", item, StorageManager.getInstance().addTo(channel , item, count));
		} else if(command.startsWith("storage take")) {
			String args = command.substring(13).trim().toLowerCase();
			String item;

			int lastIndex = args.lastIndexOf(' ');
			int count = 1;
			try {
				count = Integer.parseInt(args.substring(lastIndex + 1));
				item = args.substring(0, lastIndex);
			} catch(NumberFormatException exception) {
				item = args;
			}
			if(StorageManager.getInstance().takeFrom(channel, item, count)) {
				return String.format("**%s**: %d", item, StorageManager.getInstance().getStorageFrom(channel , item));
			} else {
				return String.format("**%s** not found in this count.", item);
			}
		}
		return "Syntax Error!";
	}

	private String listContent(TextChannel channel) {
		Map<String, Long> storage = StorageManager.getInstance().getStorageFrom(channel);
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
