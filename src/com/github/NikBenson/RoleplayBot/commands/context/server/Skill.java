package com.github.NikBenson.RoleplayBot.commands.context.server;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.ServerContext;
import com.github.NikBenson.RoleplayBot.roleplay.Character;
import com.github.NikBenson.RoleplayBot.roleplay.Skills;
import com.github.NikBenson.RoleplayBot.users.Player;
import com.github.NikBenson.RoleplayBot.users.PlayerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Map;

public class Skill extends Command<ServerContext> {
	@Override
	public String getRegex() {
		return "skill [a-z]*( add)?";
	}

	@Override
	public String execute(String command, ServerContext context) {
		MessageReceivedEvent event = (MessageReceivedEvent) context.getParams().get("event");
		Player player = PlayerManager.getInstance().getPlayerOrCreate(event.getAuthor());
		Character character = player.getCurrentCharacter();
		Skills skills = character.getSkills();

		if(command.equals("skill list")) {
			String list = "";

			Map<String, Long> all = skills.getSkills();
			long openPoints = skills.getOpenPoints();

			for(String skill : all.keySet()) {
				list += String.format("**%s**: %d\n", skill, all.get(skill));
			}
			list += String.format("\n**open skill points**: %d", openPoints);

			return list;
		} else if(command.endsWith(" add")) {
			String skill = command.substring(6, command.length() - 4);

			return skills.add(skill);
		} else {
			String skill = command.substring(6);

			return String.valueOf(skills.get(skill));
		}
	}
}
