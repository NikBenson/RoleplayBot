package com.github.nikbenson.roleplaybot.commands;

import com.github.nikbenson.roleplaybot.commands.context.Context;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public abstract class Command<E extends Context> {
	private static final Node<Context> allHead = new Node<>(Context.class);

	private static class Node<T extends Context> {
		private final Class<T> context;
		private final List<Node<T>> children = new LinkedList<>();
		private final List<Command<T>> commands = new LinkedList<>();

		public Node(Class<T> context) {
			this.context = context;
		}

		public void addCommand(Command<T> command) {
			if(this.context.equals(context)) {
				if(!commands.contains(command)) {
					commands.add(command);
				}
			} else {
				boolean addedToChild = false;
				for(Node child : children) {
					if(command.getContext().isNestmateOf(child.context)) {
						child.addCommand(command);
						addedToChild = true;
					}
				}
				if(!addedToChild) {
					Node<T> newNode = new Node<>(command.getContext());
					newNode.addCommand(command);
					children.add(newNode);
				}
			}
		}

		public Command<T> findCommand(Class<T> context, String query) {
			for(Node<T> child : children) {
				if(child.context.isNestmateOf(context)) {
					Command<T> found = child.findCommand(context, query);
					if (found != null) {
						return found;
					}
				}
			}

			for (Command<T> command : commands) {
				if(query.matches(command.getRegex())) {
					return command;
				}
			}
			return null;
		}
	}

	public static <T extends Context> Command<T> find(Class<T> context, @NotNull String query) {
		return (Command<T>) allHead.findCommand((Class<Context>) context, query);
	}

	public static void register(@NotNull Command... commands) {
		for(Command command : commands) {
			allHead.addCommand((Command<Context>) command);
		}
	}

	public abstract Class<E> getContext();

	public abstract String getRegex();
	public abstract String execute(String command, E context);
}
