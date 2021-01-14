package com.github.NikBenson.RoleplayBot.serverCommands;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.PrivateContext;
import com.github.NikBenson.RoleplayBot.commands.context.ServerContext;
import com.github.NikBenson.RoleplayBot.users.Player;
import com.github.NikBenson.RoleplayBot.users.PlayerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class MessageManager extends ListenerAdapter {
	private final String COMMAND_PREFIX;

	public MessageManager(String command_prefix) {
		COMMAND_PREFIX = command_prefix;
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
		String query = event.getMessage().getContentRaw().substring(1);

		Command<ServerContext> command = Command.find(ServerContext.class, query);

		if(command != null) {
			ServerContext context = new ServerContext(event);
			TextChannel channel = event.getTextChannel();

			channel.sendMessage(command.execute(query, context)).queue();
		}
	}

	@SubscribeEvent
	@Override
	public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
		if(!event.getAuthor().isBot()) {
			Message message = event.getMessage();
			String content = message.getContentRaw();

			if (content.startsWith(COMMAND_PREFIX)) {
				onPrivateCommand(event);
			} else {
				Player player = PlayerManager.getInstance().getPlayerOrCreate(event.getAuthor());
				if (player.isCreatingCharacter()) {
					PrivateChannel channel = event.getChannel();

					channel.sendMessage(player.characterCreationAnswer(content)).queue();
				}
			}
		}
	}
	private void onPrivateCommand(@NotNull PrivateMessageReceivedEvent event) {
		String query = event.getMessage().getContentRaw().substring(1);

		Command<PrivateContext> command = Command.find(PrivateContext.class, query);

		if(command != null) {
			PrivateContext context = new PrivateContext(event);
			PrivateChannel channel = event.getChannel();

			channel.sendMessage(command.execute(query, context)).queue();
		}
	}
}