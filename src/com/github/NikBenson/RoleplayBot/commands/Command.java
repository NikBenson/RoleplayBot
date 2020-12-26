package com.github.NikBenson.RoleplayBot.commands;

import com.github.NikBenson.RoleplayBot.commands.context.Context;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public abstract class Command<E extends Context> {
	private static final List<Command> all = new LinkedList<>();

	public static <T extends Context> Command<T> find(@NotNull String query) {
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
	public abstract String execute(String command, E context);
}
