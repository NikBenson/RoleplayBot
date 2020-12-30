package com.github.NikBenson.RoleplayBot.roleplay;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.github.NikBenson.RoleplayBot.json.ConfigLoader.readJsonFromFile;

public class StorageManager extends ListenerAdapter {
	private static Map<TextChannel, Map<String, Long>> storages = new HashMap<>();

	public static long addTo(TextChannel channel, String item) {
		Map<String, Long> storage = getStorageOrCreateFrom(channel);

		if(storage.containsKey(item)) {
			storage.replace(item, storage.get(item) + 1);
		} else {
			storage.put(item, 1l);
		}

		return storage.get(item);
	}

	public static boolean takeFrom(TextChannel channel, String item) {
		Map<String, Long> storage = getStorageOrCreateFrom(channel);

		if(storage.containsKey(item)) {
			storage.replace(item, storage.get(item) - 1);

			if(storage.get(item) <= 0) {
				storage.remove(item);
			}

			return true;
		}

		return false;
	}

	public static Map<String, Long> getStorageFrom(TextChannel channel) {
		return getStorageOrCreateFrom(channel);
	}
	public static long getStorageFrom(TextChannel channel, String item) {
		return getStorageOrCreateFrom(channel).getOrDefault(item, 0l);
	}

	public static void saveTo(File file) throws IOException {
		JSONObject storagesJson = new JSONObject();

		for(TextChannel channel : storages.keySet()) {
			JSONObject storageJson = (JSONObject) storages.get(channel);

			storagesJson.put(channel.getId(), storageJson);
		}

		Files.writeString(Path.of(file.getPath()), storagesJson.toJSONString());
	}
	public static void loadFrom(File file, JDA jda) throws IOException, ParseException {
		JSONObject storagesJson = readJsonFromFile(file);

		for(Object channelId : storagesJson.keySet()) {
			TextChannel channel = jda.getTextChannelById((String) channelId);

			Map<String, Long> storage = (Map<String, Long>) storagesJson.get(channelId);

			storages.put(channel, storage);
		}
	}

	private static Map<String, Long> getStorageOrCreateFrom(TextChannel channel) {
		if(!storages.containsKey(channel)) {
			storages.put(channel, new JSONObject());
		}

		return storages.get(channel);
	}

	private final File configurationFile;

	public StorageManager(File configurationFile) {
		this.configurationFile = configurationFile;
	}

	@SubscribeEvent
	@Override
	public void onShutdown(@NotNull ShutdownEvent event) {
		try {
			if(!configurationFile.exists()) {
				Files.createFile(configurationFile.toPath());
			}

			saveTo(configurationFile);
		} catch (IOException e) {
			System.out.println("Could not save storages!");
			e.printStackTrace();
		}
	}
	@SubscribeEvent
	@Override
	public void onReady(@NotNull ReadyEvent event) {
		if(configurationFile.exists()) {
			try {
				loadFrom(configurationFile, event.getJDA());
			} catch (Exception e) {
				System.out.println("Could not load existing storage!");
				e.printStackTrace();
			}
		}
	}
}
