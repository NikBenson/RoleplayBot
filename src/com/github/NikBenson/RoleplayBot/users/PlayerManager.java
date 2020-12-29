package com.github.NikBenson.RoleplayBot.users;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static com.github.NikBenson.RoleplayBot.json.JSONFileReader.getJson;

public class PlayerManager extends ListenerAdapter {
	private static PlayerManager instance;

	public static PlayerManager createInstance(File configurationFile, JDA jda) {
		instance = new PlayerManager(configurationFile, jda);

		return getInstance();
	}
	public static PlayerManager getInstance() {
		return instance;
	}

	Map<User, Player> players = new HashMap<>();

	private final File configurationFile;

	private PlayerManager(File configurationFile, JDA jda) {
		this.configurationFile = configurationFile;

		try {
			if(configurationFile.exists()) {
				JSONArray json = (JSONArray) getJson(configurationFile).get("players");

				for (int i = 0; i < json.size(); i++) {
					JSONObject currentJson = (JSONObject) json.get(i);

					User user = jda.getUserById((String) currentJson.get("id"));
					Player currentPlayer = new Player(currentJson);

					players.put(user, currentPlayer);
				}
			}
		} catch (Exception e) {
			System.out.println("Could not read saved players!");
			e.printStackTrace();
		}
	}

	public Player getPlayer(User user) {
		if(!players.containsKey(user)) {
			players.put(user, new Player(user));
		}

		return players.get(user);
	}

	@SubscribeEvent
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		User user = event.getUser();

		if(!user.isBot()) {
			if(!players.containsKey(user)) {
				players.put(user, new Player(user));
			}
		}
	}

	@SubscribeEvent
	@Override
	public void onShutdown(@NotNull ShutdownEvent event) {
		savePlayers();
	}

	private void savePlayers() {
		JSONArray playersJson = new JSONArray();

		for(User user : players.keySet()) {
			Player player = players.get(user);
			playersJson.add(player.getJson());
		}

		JSONObject json = new JSONObject();

		json.put("players", playersJson);

		try {
			if(!configurationFile.exists()) {
				Files.createFile(configurationFile.toPath());
			}

			Files.writeString(configurationFile.toPath(), json.toJSONString());

		} catch (IOException e) {
			System.out.println("Could not save players!");
			e.printStackTrace();
		}
	}

}
