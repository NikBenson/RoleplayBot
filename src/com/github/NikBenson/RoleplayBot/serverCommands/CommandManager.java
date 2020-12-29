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

public class CommandManager extends ListenerAdapter {

	@SubscribeEvent
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if(!event.getAuthor().isBot() && event.isFromGuild()) {
			Message message = event.getMessage();
			String content = message.getContentRaw();

			if(content.startsWith("!")) {
				onCommand(event);
			}
		}
	}
	private void onCommand(MessageReceivedEvent event) {
		String query = event.getMessage().getContentRaw().substring(1);

		Command<ServerContext> command = Command.find(query);

		if(command != null) {
			ServerContext context = new ServerContext(event);
			TextChannel channel = event.getTextChannel();

			channel.sendMessage(command.execute(query, context)).queue();
		}
	}

	@SubscribeEvent
	@Override
	public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
		Message message = event.getMessage();
		String content = message.getContentRaw();

		if(content.startsWith("!")) {
			onPrivateCommand(event);
		} else {
			Player player = PlayerManager.getInstance().getPlayer(event.getAuthor());
			if (player.isCreatingCharacter()) {
				player.characterCreationAnswer(content);
			}
		}
	}
	private void onPrivateCommand(@NotNull PrivateMessageReceivedEvent event) {
		String query = event.getMessage().getContentRaw().substring(1);

		Command<PrivateContext> command = Command.find(query);

		if(command != null) {
			PrivateContext context = new PrivateContext(event);
			PrivateChannel channel = event.getChannel();

			channel.sendMessage(command.execute(query, context)).queue();
		}
	}
}
