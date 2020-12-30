package com.github.NikBenson.RoleplayBot.roleplay;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Team {
	private static String question;

	private static List<Team> all = new LinkedList<>();

	public static List<Team> getAll() {
		return all;
	}

	public static Team findTeam(String name) {
		for(Team team : all) {
			if(team.getName().equals(name)) {
				return team;
			}
		}

		return null;
	}

	public static void setQuestion(String question) {
		Team.question = question;
	}
	public static String getQuestion() {
		return question;
	}

	private final String name;
	private final TextChannel sheetsChannel;
	private final Role role;

	private List<Character> members = new LinkedList<Character>();

	public Team(JSONObject json, JDA jda) {
		name = (String) json.get("name");
		sheetsChannel = jda.getTextChannelById((Long) json.get("sheetChannel"));
		role = jda.getRoleById((Long) json.get("role"));

		all.add(this);
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
