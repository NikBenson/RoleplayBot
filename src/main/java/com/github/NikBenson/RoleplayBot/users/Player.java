package com.github.NikBenson.RoleplayBot.users;

import com.github.NikBenson.RoleplayBot.messages.WelcomeMessenger;
import com.github.NikBenson.RoleplayBot.roleplay.character.Character;
import com.github.NikBenson.RoleplayBot.roleplay.character.SheetBlueprint;
import com.github.NikBenson.RoleplayBot.roleplay.character.Team;
import com.github.NikBenson.RoleplayBot.roleplay.character.TeamsManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import net.dv8tion.jda.api.entities.User;

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
		WelcomeMessenger.getInstanceOrCreate().sendTo(user);
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
		return TeamsManager.getInstance().getQuestion();
	}
	public void cancelCharacterCreation() {
		characterCreationPhase = -1;
		creatingCharacterSheet = null;
		creatingCharacterTeam = null;
	}
	public String characterCreationAnswer(String answer) {
		if(creatingCharacterTeam == null) {
			creatingCharacterTeam = TeamsManager.getInstance().findTeam(answer);

			if(creatingCharacterTeam != null) {
				return SheetBlueprint.getInstanceOrCreate().getSheetQuestion(characterCreationPhase);
			} else {
				return "Invalid team. Please try again!";
			}
		} else {
			creatingCharacterSheet.put(SheetBlueprint.getInstanceOrCreate().getSheetAttribute(characterCreationPhase), answer);
			characterCreationPhase++;

			if (SheetBlueprint.getInstanceOrCreate().getSheetAttribute(characterCreationPhase) != null) {
				return SheetBlueprint.getInstanceOrCreate().getSheetQuestion(characterCreationPhase);
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

	public JSONObject getJSON() {
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
