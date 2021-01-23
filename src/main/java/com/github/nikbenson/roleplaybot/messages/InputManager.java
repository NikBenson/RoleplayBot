package com.github.nikbenson.roleplaybot.messages;

import com.github.nikbenson.roleplaybot.commands.Command;
import com.github.nikbenson.roleplaybot.commands.context.CLIContext;
import com.github.nikbenson.roleplaybot.commands.context.GuildMessageContext;
import com.github.nikbenson.roleplaybot.commands.context.PrivateMessageContext;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
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
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		if(!event.getAuthor().isBot()) {
			Message message = event.getMessage();
			String content = message.getContentRaw();

			if(content.startsWith(COMMAND_PREFIX)) {
				onCommand(event);
			}
		}
	}
	private void onCommand(GuildMessageReceivedEvent event) {
		String query = event.getMessage().getContentRaw().substring(COMMAND_PREFIX.length());

		Command<GuildMessageContext> command = Command.find(GuildMessageContext.class, query);

		if(command != null) {
			GuildMessageContext context = new GuildMessageContext(event);
			TextChannel channel = event.getChannel();

			channel.sendMessage(command.execute(query, context)).queue();
		}
	}

	@SubscribeEvent
	@Override
	public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
		Message message = event.getMessage();
		String content = message.getContentRaw();

		if(content.startsWith(COMMAND_PREFIX)) {
			onCommand(event);
		}
	}
	private void onCommand(PrivateMessageReceivedEvent event) {
		String query = event.getMessage().getContentRaw().substring(COMMAND_PREFIX.length());

		Command<PrivateMessageContext> command = Command.find(PrivateMessageContext.class, query);

		if(command != null) {
			PrivateMessageContext context = new PrivateMessageContext(event);
			PrivateChannel channel = event.getChannel();

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
