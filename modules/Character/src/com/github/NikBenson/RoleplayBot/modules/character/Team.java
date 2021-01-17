package com.github.NikBenson.RoleplayBot.modules.character;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Team {

	private final String name;
	private final TextChannel sheetsChannel;
	private final Role role;

	private List<java.lang.Character> members = new LinkedList<java.lang.Character>();

	public Team(JSONObject json, JDA jda) {
		name = (String) json.get("name");
		sheetsChannel = jda.getTextChannelById((Long) json.get("sheetChannel"));
		role = jda.getRoleById((Long) json.get("role"));
	}

	public String getName() {
		return name;
	}

	public Role getRole() {
		return role;
	}

	public void registerCharacter(java.lang.Character character) {
		members.add(character);

		sendSheet(character);
	}

	private void sendSheet(java.lang.Character character) {
		String sheetMessage = "";

		Map<String, String> characterSheet = character.getSheet();

		for(String attribute : characterSheet.keySet()) {
			String value = characterSheet.get(attribute);

			sheetMessage += String.format("**%s**: %s\n", attribute, value);
		}

		sheetsChannel.sendMessage(sheetMessage).queue();
	}
}
