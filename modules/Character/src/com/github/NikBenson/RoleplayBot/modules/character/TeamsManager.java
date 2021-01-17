package com.github.NikBenson.RoleplayBot.modules.character;

import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import com.github.NikBenson.RoleplayBot.modules.ModulesManager;
import com.github.NikBenson.RoleplayBot.modules.player.models.Team;
import net.dv8tion.jda.api.JDA;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class TeamsManager implements JSONConfigured {
	private static TeamsManager instance;

	public static TeamsManager createInstance(JDA jda) {
		if (instance == null) {
			instance = new TeamsManager(jda);
		}

		return getInstance();
	}

	public static TeamsManager getInstance() {
		return instance;
	}

	private final net.dv8tion.jda.api.JDA JDA;

	private String question;

	private List<Team> all = new LinkedList<>();

	private TeamsManager(JDA jda) {
		JDA = jda;

		ModulesManager.registerModule("teams", this);
	}

	public List<Team> getAll() {
		return all;
	}

	public Team findTeam(String name) {
		for(Team team : all) {
			if(team.getName().equals(name)) {
				return team;
			}
		}

		return null;
	}

	public String getQuestion() {
		return question;
	}
	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public File getConfigPath() {
		return new File(ConfigurationManager.getInstance().getConfigurationRootPath(), ConfigurationPaths.TEAMS_FILE);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		question = (String) json.get("question");

		JSONArray teams = (JSONArray) json.get("teams");

		for (Object team : teams) {
			all.add(new Team((JSONObject) team, JDA));
		}
	}
}
