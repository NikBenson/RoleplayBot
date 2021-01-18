package com.github.NikBenson.RoleplayBot.messages;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.CLIContext;
import com.github.NikBenson.RoleplayBot.commands.context.ServerContext;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class InputManager extends ListenerAdapter implements CLIEventListener {
	private final String COMMAND_PREFIX;

	public InputManager(String command_prefix) {
		COMMAND_PREFIX = command_prefix;

		CLIEventGenerator.addListener(this);
	}

	@SubscribeEvent
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if(!event.getAuthor().isBot() && event.isFromGuild()) {
			Message message = event.getMessage();
			String content = message.getContentRaw();

			if(content.startsWith(COMMAND_PREFIX)) {
				onCommand(event);
			}
		}
	}
	private void onCommand(MessageReceivedEvent event) {
		String query = event.getMessage().getContentRaw().substring(COMMAND_PREFIX.length());

		Command<ServerContext> command = Command.find(ServerContext.class, query);

		if(command != null) {
			ServerContext context = new ServerContext(event);
			TextChannel channel = event.getTextChannel();

			channel.sendMessage(command.execute(query, context)).queue();
		}
	}

	@Override
	public void onLineEntered(String query) {
		Command<CLIContext> command = Command.find(CLIContext.class, query);

		if(command != null) {
			CLIContext context = new CLIContext();

			System.out.println(command.execute(query, context));
		} else {
			System.out.println("Command not found!");
		}

	}
}
