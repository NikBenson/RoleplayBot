package com.github.NikBenson.RoleplayBot.modules.character;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.PrivateContext;
import com.github.NikBenson.RoleplayBot.modules.RoleplayBotModule;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class CharacterModule extends ListenerAdapter implements RoleplayBotModule {
	@Override
	public boolean isActive(Guild guild) {
		return false;
	}

	@Override
	public void load(Guild guild) {

	}

	@Override
	public void unload(Guild guild) {

	}

	@SubscribeEvent
	@Override
	public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			Message message = event.getMessage();
			String content = message.getContentRaw();

			if (content.startsWith(String.valueOf('!'))) {
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
