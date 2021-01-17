package com.github.NikBenson.RoleplayBot.modules.character.commands;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.ServerContext;
import com.github.NikBenson.RoleplayBot.roleplay.character.SheetBlueprint;
import com.github.NikBenson.RoleplayBot.roleplay.character.Skills;
import com.github.NikBenson.RoleplayBot.users.Player;
import com.github.NikBenson.RoleplayBot.users.PlayerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Map;

public class Skill extends Command<ServerContext> {
	@Override
	public Class<ServerContext> getContext() {
		return ServerContext.class;
	}

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
			StringBuilder list = new StringBuilder();

			try {
				list.append(String.format("**%s**\n\n", character.getAttribute(SheetBlueprint.getInstanceOrCreate().getSheetAttribute(0))));
			} catch (IndexOutOfBoundsException e) {

			}

			Map<String, Long> all = skills.getSkills();
			long openPoints = skills.getOpenPoints();

			for(String skill : all.keySet()) {
				list.append(String.format("**%s**: %d\n", skill, all.get(skill)));
			}
			list.append(String.format("\n**open skill points**: %d", openPoints));

			return list.toString();
		} else if(command.endsWith(" add")) {
			String skill = command.substring(6, command.length() - 4);

			return skills.add(skill);
		} else {
			String skill = command.substring(6);

			return String.valueOf(skills.get(skill));
		}
	}
}
