package com.github.NikBenson.RoleplayBot.users;

import com.github.NikBenson.RoleplayBot.messages.WelcomeMessenger;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager extends ListenerAdapter {
	Map<User, Player> users = new HashMap<>();

	@SubscribeEvent
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		User user = event.getUser();

		if(!user.isBot()) {
			if(!users.containsKey(user)) {
				users.put(user, new Player(user));
				WelcomeMessenger.sendTo(user);
			}
		}
	}
}
