package com.github.NikBenson.RoleplayBot;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.Context;
import com.github.NikBenson.RoleplayBot.commands.context.general.*;
import com.github.NikBenson.RoleplayBot.commands.context.privateMessage.CancelCharacter;
import com.github.NikBenson.RoleplayBot.commands.context.privateMessage.CreateCharacter;
import com.github.NikBenson.RoleplayBot.commands.context.server.Shutdown;
import com.github.NikBenson.RoleplayBot.commands.context.server.Skill;
import com.github.NikBenson.RoleplayBot.commands.context.server.Storage;
import com.github.NikBenson.RoleplayBot.commands.context.user.PlayerName;
import com.github.NikBenson.RoleplayBot.messages.MessageFormatter;
import com.github.NikBenson.RoleplayBot.messages.RepeatedMessage;
import com.github.NikBenson.RoleplayBot.messages.WelcomeMessenger;
import com.github.NikBenson.RoleplayBot.roleplay.Character;
import com.github.NikBenson.RoleplayBot.roleplay.*;
import com.github.NikBenson.RoleplayBot.serverCommands.CommandManager;
import com.github.NikBenson.RoleplayBot.users.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.github.NikBenson.RoleplayBot.json.JSONFileReader.getJson;

public class Bot {
	private final String configurationDirectoryPath;
	private final String autogeneratedConfigurationPath;

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
		Command.register(new PlayerName());
		Command.register(new Storage());
		Command.register(new Shutdown());
		Command.register(new CreateCharacter());
		Command.register(new CancelCharacter());
		Command.register(new Skill());
	}


	private Bot(String configurationDirectoryPath) {
		this.configurationDirectoryPath = configurationDirectoryPath;

		File autogeneratedConfig = new File(configurationDirectoryPath, ".autogenerated");

		try {
			init(getJson(new File(configurationDirectoryPath, "botinfo.json")));

			if(!autogeneratedConfig.exists()) {
				Files.createDirectory(autogeneratedConfig.toPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		autogeneratedConfigurationPath = autogeneratedConfig.getPath();

		File welcomeMessageConfigurationFile = new File(configurationDirectoryPath, "welcomemessage.json");

		if(welcomeMessageConfigurationFile.exists()) {
			try {
				WelcomeMessenger.init(getJson(welcomeMessageConfigurationFile));
			} catch (Exception e) {
				System.out.println("No welcome message configuration found.");
			}
		}

		GameManager.setInstance(configurationDirectoryPath);

		registerRepeatedMessages();

		try {
			loadPlayers();
		} catch (Exception e) {
			System.out.println("Could not load charactergeneration.json");
			e.printStackTrace();
		}
	}

	private void loadPlayers() throws IOException, ParseException {
		File file = new File(configurationDirectoryPath, "charactergeneration,json");

		if(loadTeams()) {
			PlayerManager.createInstance(new File(autogeneratedConfigurationPath, "players.json"), jda);
			if(file.exists()) {

				JSONObject json = getJson(file);
				JSONArray sheet = (JSONArray) json.get("sheet");

				List<String> attributes = new LinkedList<>();
				List<String> questions = new LinkedList<>();

				for (int i = 0; i < sheet.size(); i++) {
					JSONObject current = (JSONObject) sheet.get(i);

					attributes.add((String) current.get("name"));
					questions.add((String) current.get("question"));
				}

				Character.setSheetAttributes(attributes);
				Character.setSheetQuestions(questions);

				List<String> skills = (JSONArray) json.get("skills");

				Skills.setAllSkills(skills);
			}
		}
	}
	private boolean loadTeams() throws IOException, ParseException {
		File file = new File(configurationDirectoryPath, "teams.json");

		if(file.exists()) {
			JSONObject json = getJson(file);

			Team.setQuestion((String) json.get("question"));

			JSONArray teams = (JSONArray) json.get("teams");

			for (int i = 0; i < teams.size(); i++) {
				new Team((JSONObject) teams.get(i), jda);
			}

			return true;
		}

		return false;
	}

	private void init(JSONObject params) throws LoginException, InterruptedException, IOException {
		JDABuilder builder = JDABuilder.createDefault((String) params.get("token"));

		builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
		builder.setBulkDeleteSplittingEnabled(false);
		builder.setCompression(Compression.NONE);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);

		if (params.get("playing") != null) {
			builder.setActivity(Activity.playing((String) params.get("playing")));
		} else if (params.get("listening") != null) {
			builder.setActivity(Activity.playing((String) params.get("listening")));
		} else if (params.get("streaming") != null) {
			builder.setActivity(Activity.playing((String) params.get("streaming")));
		} else if (params.get("watching") != null) {
			builder.setActivity(Activity.playing((String) params.get("watching")));
		}

		jda = builder.build();

		jda.addEventListener(new CommandManager(), new StorageManager(new File(autogeneratedConfigurationPath, "storage.json")));

		jda.awaitReady();
	}

	private void registerRepeatedMessages() {
		File messagesDirectory = new File(configurationDirectoryPath, "RepeatedMessages");
		if (messagesDirectory.exists()) {
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
		for (int i = 0; i < values.length; i++) {
			values[i] = (String) valuesJSON.get(i);
		}

		MessageFormatter<Context> messageFormatter = new MessageFormatter<>(message, values);
		new RepeatedMessage(channel, messageFormatter, startAt, timeDelta);
	}
}