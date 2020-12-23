package com.github.NikBenson.RoleplayBot;

import com.github.NikBenson.RoleplayBot.messages.MessageFormatter;
import com.github.NikBenson.RoleplayBot.messages.RepeatedMessage;
import com.github.NikBenson.RoleplayBot.messages.commands.*;
import com.github.NikBenson.RoleplayBot.roleplay.GameManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.github.NikBenson.RoleplayBot.json.JSONFileReader.getJson;

public class Bot {
	private final String configurationDirectoryPath;

	private JDA jda;

	public static void main(String[] args) {
		registerCommands();
		new Bot(args[0]);
	}
	private static void registerCommands() {
		Command.register(new DateNow());
		Command.register(new IngameDay());
		Command.register(new IngameSeason());
		Command.register(new IngameLightLevel());
		Command.register(new IngameWeather());
		Command.register(new IngameTemperarture());
		Command.register(new IngameTime());
	}


	private Bot(String configurationDirectoryPath) {
		this.configurationDirectoryPath = configurationDirectoryPath;
		try {
			init(getJson(new File(configurationDirectoryPath, "botinfo.json")));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		GameManager.setInstance(configurationDirectoryPath);

		registerRepeatedMessages();
	}

	private void init(JSONObject params) throws LoginException, InterruptedException {
		JDABuilder builder = JDABuilder.createDefault((String) params.get("token"));

		builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
		builder.setBulkDeleteSplittingEnabled(false);
		builder.setCompression(Compression.NONE);

		if(params.get("playing") != null) {
			builder.setActivity(Activity.playing((String) params.get("playing")));
		} else if(params.get("listening") != null) {
			builder.setActivity(Activity.playing((String) params.get("listening")));
		} else if(params.get("streaming") != null) {
			builder.setActivity(Activity.playing((String) params.get("streaming")));
		} else if(params.get("watching") != null) {
			builder.setActivity(Activity.playing((String) params.get("watching")));
		}

		jda = builder.build();

		jda.awaitReady();
	}

	private void registerRepeatedMessages() {
		File messagesDirectory = new File(configurationDirectoryPath, "RepeatedMessages");
		if(messagesDirectory.exists()) {
			for (File messageFile : messagesDirectory.listFiles()) {
				loadRepeatedMessageFromFile(messageFile);
			}
		}
	}
	private void loadRepeatedMessageFromFile(File file) {
		try {
			JSONObject json = getJson(file);
			loadRepeatedMessageFromJSON(json);
		} catch (Exception e) {
			System.out.println("Could not load RepeatedMessage at " + file.getPath());
			e.printStackTrace();
		}
	}
	private void loadRepeatedMessageFromJSON(JSONObject json) throws java.text.ParseException {
		TextChannel channel = jda.getTextChannelById((Long) json.get("channel"));
		Date startAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) json.get("startAt"));
		long timeDelta = (long) json.get("timeDelta");

		String message = (String) json.get("message");
		JSONArray valuesJSON = (JSONArray) json.get("values");
		String[] values = new String[valuesJSON.size()];
		for(int i = 0; i < values.length; i++) {
			values[i] = (String) valuesJSON.get(i);
		}

		MessageFormatter messageFormatter = new MessageFormatter(message, values);
		new RepeatedMessage(channel, messageFormatter, startAt, timeDelta);
	}

}
