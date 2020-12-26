package com.github.NikBenson.RoleplayBot.roleplay;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class StorageManager {
	private static Map<TextChannel, Map<String, Integer>> storages = new HashMap<>();

	public static int addTo(TextChannel channel, String item) {
		Map<String, Integer> storage = getStorageOrCreateFrom(channel);

		if(storage.containsKey(item)) {
			storage.replace(item, storage.get(item) + 1);
		} else {
			storage.put(item, 1);
		}

		return storage.get(item);
	}

	public static boolean takeFrom(TextChannel channel, String item) {
		Map<String, Integer> storage = getStorageOrCreateFrom(channel);

		if(storage.containsKey(item)) {
			storage.replace(item, storage.get(item) - 1);

			if(storage.get(item) <= 0) {
				storage.remove(item);
			}

			return true;
		}

		return false;
	}

	public static Map<String, Integer> getStorageFrom(TextChannel channel) {
		return getStorageOrCreateFrom(channel);
	}
	public static int getStorageFrom(TextChannel channel, String item) {
		return getStorageOrCreateFrom(channel).getOrDefault(item, 0);
	}

	private static Map<String, Integer> getStorageOrCreateFrom(TextChannel channel) {
		if(!storages.containsKey(channel)) {
			storages.put(channel, new HashMap<>());
		}

		return storages.get(channel);
	}

	private StorageManager() {

	}
}
