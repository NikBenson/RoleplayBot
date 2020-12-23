package com.github.NikBenson.RoleplayBot.messages.commands;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public abstract class Command {
	private static final List<Command> all = new LinkedList<>();

	public static Command find(@NotNull String query) {
		for (Command command : all) {
			if(query.matches(command.getRegex())) {
				return command;
			}
		}
		return null;
	}

	public static void register(@NotNull Command command) {
		for (Command existing : all) if(command.getClass().equals(existing.getClass())) return;
		all.add(command);
	}

	public abstract String getRegex();
	public abstract String execute(String command);
}
