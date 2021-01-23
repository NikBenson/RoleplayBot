package com.github.nikbenson.roleplaybot.messages;

import com.github.nikbenson.roleplaybot.commands.Command;
import com.github.nikbenson.roleplaybot.commands.context.Context;

public class MessageFormatter<E extends Context> {
	private final String unformattedMessage;
	private final Command<E>[] commandHandlers;
	private final String[] commands;

	public MessageFormatter(Class<E> context, String message, String... values) {
		unformattedMessage = message;

		commands = new String[values.length];
		commandHandlers = new Command[values.length];

		for(int i = 0; i < values.length; i++) {
			commands[i] = values[i];
			commandHandlers[i] = Command.find(context, commands[i]);
		}
	}

	public String createMessage(E context) {
			return formatMessage(context);
	}

	private String formatMessage(E context) {
		return String.format(unformattedMessage, (Object[]) executeCommands(context));
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
