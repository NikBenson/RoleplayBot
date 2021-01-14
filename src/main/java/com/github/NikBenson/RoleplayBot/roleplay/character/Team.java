package com.github.NikBenson.RoleplayBot.roleplay.character;

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

	private List<Character> members = new LinkedList<Character>();

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

	public void registerCharacter(Character character) {
		members.add(character);

		sendSheet(character);
	}

	private void sendSheet(Character character) {
		String sheetMessage = "";

		Map<String, String> characterSheet = character.getSheet();

		for(String attribute : characterSheet.keySet()) {
			String value = characterSheet.get(attribute);

			sheetMessage += String.format("**%s**: %s\n", attribute, value);
		}

		sheetsChannel.sendMessage(sheetMessage).queue();
	}
}
