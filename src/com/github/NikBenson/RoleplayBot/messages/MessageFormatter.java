package com.github.NikBenson.RoleplayBot.messages;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.Context;

public class MessageFormatter<E extends Context> {
	private String unformattedMessage;
	private Command[] commandHandlers;
	private String[] commands;

	public MessageFormatter(String message, String... values) {
		unformattedMessage = message;

		commands = new String[values.length];
		commandHandlers = new Command[values.length];

		for(int i = 0; i < values.length; i++) {
			commands[i] = values[i];
			commandHandlers[i] = Command.find(Context.class, commands[i]);
		}
	}

	public String createMessage(E context) {
			return formatMessage(context);
	}

	private String formatMessage(E context) {
		return String.format(unformattedMessage, executeCommands(context));
	}

	private String[] executeCommands(E context) {
		String[] executedCommands = new String[commandHandlers.length];

		for(int i = 0; i < commandHandlers.length; i++) {
			if (commandHandlers[i] != null) {
				executedCommands[i] = commandHandlers[i].execute(commands[i], context);
			}
			else {
				System.out.println("Could not find any command matching: " + commands[i]);
				executedCommands[i] = commands[i];
			}
		}

		return executedCommands;
	}
}
