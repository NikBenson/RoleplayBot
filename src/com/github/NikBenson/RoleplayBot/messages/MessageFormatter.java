package com.github.NikBenson.RoleplayBot.messages;

import com.github.NikBenson.RoleplayBot.messages.commands.Command;

public class MessageFormatter {
	private String unformattedMessage;
	private Command[] commandHandlers;
	private String[] commands;

	public MessageFormatter(String message, String... values) {
		unformattedMessage = message;

		commands = new String[values.length];
		commandHandlers = new Command[values.length];

		for(int i = 0; i < values.length; i++) {
			commands[i] = values[i];
			commandHandlers[i] = Command.find(commands[i]);
		}
	}

	public String createMessage() {
			return formatMessage();
	}

	private String formatMessage() {
		return String.format(unformattedMessage, executeCommands());
	}

	private String[] executeCommands() {
		String[] executedCommands = new String[commandHandlers.length];

		for(int i = 0; i < commandHandlers.length; i++) {
			if (commandHandlers[i] != null) {
				executedCommands[i] = commandHandlers[i].execute(commands[i]);
			}
			else {
				System.out.println("Could not find any command matching: " + commands[i]);
				executedCommands[i] = commands[i];
			}
		}

		return executedCommands;
	}
}