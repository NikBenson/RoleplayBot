package com.github.NikBenson.RoleplayBot.commands;

import com.github.NikBenson.RoleplayBot.commands.context.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Command<E extends Context> {
	private static final Map<Class, List<Command>> all = new HashMap<>();

	public static <T extends Context> Command<T> find(Class<T> context, @NotNull String query) {
		for (Command command : all.get(context)) {
			if(query.matches(command.getRegex())) {
				return command;
			}
		}
		return null;
	}

	public static <T extends Context> void register(Class<T> context, @NotNull Command<T> command) {
		if(all.containsKey(context)) {
			all.put(context, new LinkedList<>());
		}

		for (Command existing : all.get(context)) {
			if(command.getClass().equals(existing.getClass())) return;
		}

		all.get(context).add(command);
	}

	public abstract String getRegex();
	public abstract String execute(String command, E context);
}
