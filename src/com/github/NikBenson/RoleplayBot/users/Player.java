package com.github.NikBenson.RoleplayBot.users;

import com.github.NikBenson.RoleplayBot.messages.WelcomeMessenger;
import com.github.NikBenson.RoleplayBot.roleplay.Character;
import com.github.NikBenson.RoleplayBot.roleplay.Team;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Player {
	private List<Character> characters = new LinkedList<>();
	private int currentCharacter = 0;

	private int characterCreationPhase = -1;
	private Map<String, String> creatingCharacterSheet;
	private Team creatingCharacterTeam;

	private final String userId;

	public Player(User user) {
		userId = user.getId();
		WelcomeMessenger.sendTo(user);
	}

	public Player(JSONObject json) {
		userId = (String) json.get("id");

		JSONArray charactersJson = (JSONArray) json.get("characters");

		for (int i = 0; i < charactersJson.size(); i++) {
			JSONObject characterJson = (JSONObject) charactersJson.get(i);

			characters.add(new Character(characterJson));
		}
	}

	public Character getCurrentCharacter() {
		return characters.get(currentCharacter);
	}

	public String startCharacterCreation() {
		characterCreationPhase = 0;
		creatingCharacterSheet = new JSONObject();
		return Team.getQuestion();
	}
	public void cancelCharacterCreation() {
		characterCreationPhase = -1;
		creatingCharacterSheet = null;
		creatingCharacterTeam = null;
	}
	public String characterCreationAnswer(String answer) {
		if(creatingCharacterTeam == null) {
			creatingCharacterTeam = Team.findTeam(answer);

			if(creatingCharacterTeam != null) {
				return Character.getSheetQuestion(characterCreationPhase);
			} else {
				return "Invalid team. Please try again!";
			}
		} else {
			creatingCharacterSheet.put(Character.getSheetAttribute(characterCreationPhase), answer);
			characterCreationPhase++;

			if (Character.getSheetAttribute(characterCreationPhase) != null) {
				return Character.getSheetQuestion(characterCreationPhase);
			} else {
				characters.add(new Character(creatingCharacterSheet, creatingCharacterTeam));
				cancelCharacterCreation();
				return "finished!";
			}
		}
	}
	public boolean isCreatingCharacter() {
		return characterCreationPhase >= 0;
	}

	public JSONObject getJson() {
		JSONObject json = new JSONObject();

		json.put("id", userId);

		JSONArray charactersJson = new JSONArray();
		for(Character character : characters) {
			charactersJson.add(character.getJson());
		}

		json.put("characters", charactersJson);

		return json;
	}
}
