package com.github.NikBenson.RoleplayBot;

import com.github.NikBenson.RoleplayBot.api.RestApiManager;
import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.general.*;
import com.github.NikBenson.RoleplayBot.commands.context.privateMessage.CancelCharacter;
import com.github.NikBenson.RoleplayBot.commands.context.privateMessage.CreateCharacter;
import com.github.NikBenson.RoleplayBot.commands.context.server.*;
import com.github.NikBenson.RoleplayBot.commands.context.user.PlayerName;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.ModulesManager;
import com.github.NikBenson.RoleplayBot.messages.RepeatedMessagesManager;
import com.github.NikBenson.RoleplayBot.messages.WelcomeMessenger;
import com.github.NikBenson.RoleplayBot.roleplay.GameManager;
import com.github.NikBenson.RoleplayBot.roleplay.StorageManager;
import com.github.NikBenson.RoleplayBot.roleplay.character.SheetBlueprint;
import com.github.NikBenson.RoleplayBot.roleplay.character.TeamsManager;
import com.github.NikBenson.RoleplayBot.serverCommands.ManualManager;
import com.github.NikBenson.RoleplayBot.serverCommands.MessageManager;
import com.github.NikBenson.RoleplayBot.users.PlayerManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

import static com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager.readJSONFromFile;

public class Bot extends ListenerAdapter {
	private JDA jda;

	private JSONObject config;

	private final String configurationDirectoryPath;

	public static void main(String[] args) {
		registerCommands();

		new Bot(args[0]);
	}
	private static void registerCommands() {
		Command.register(new DateNow(),
				new Ingame(),
				new PlayerName(),
				new Storage(),
				new Shutdown(),
				new CreateCharacter(),
				new CancelCharacter(),
				new Skill(),
				new Save(),
				new Reload(),
				new Manual());
	}


	private Bot(String configurationDirectoryPath) {
		this.configurationDirectoryPath = configurationDirectoryPath;

		try {
			loadFromJSON(readJSONFromFile(getConfigPath()));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		jda.addEventListener(this);
	}

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		try {
			ConfigurationManager.setInstance(jda, configurationDirectoryPath);
			loadDefault();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONArray modules = (JSONArray) config.getOrDefault("modules", new JSONArray());
		for (Object module : modules) {
			String moduleName = (String) module;
			ModulesManager.activateModules(moduleName);
		}

		jda.addEventListener(new MessageManager("!"));
	}

	public void loadDefault() {
		ManualManager.getInstanceOrCreate();
		GameManager.getInstanceOrCreate();
		SheetBlueprint.getInstanceOrCreate();
		TeamsManager.createInstance(jda);
		PlayerManager.createInstance(jda);
		WelcomeMessenger.getInstanceOrCreate();
		StorageManager.createInstance(jda);
		RepeatedMessagesManager.setInstance(jda);
		ModulesManager.registerModule("api", new RestApiManager());
	}

	public File getConfigPath() {
		return new File(configurationDirectoryPath, ConfigurationPaths.BOT_INFO_FILE);
	}

	public void loadFromJSON(JSONObject json) {
		config = json;
		if(jda == null) {
			JDABuilder builder = JDABuilder.createDefault((String) json.get("token"));

			builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
			builder.setBulkDeleteSplittingEnabled(false);
			builder.setCompression(Compression.NONE);
			builder.enableIntents(GatewayIntent.GUILD_MEMBERS);

			if (json.containsKey("playing")) {
				builder.setActivity(Activity.playing((String) json.get("playing")));
			} else if (json.containsKey("listening")) {
				builder.setActivity(Activity.playing((String) json.get("listening")));
			} else if (json.containsKey("streaming")) {
				builder.setActivity(Activity.playing((String) json.get("streaming")));
			} else if (json.containsKey("watching")) {
				builder.setActivity(Activity.playing((String) json.get("watching")));
			}

			try {
				jda = builder.build();
			} catch (LoginException e) {
				System.out.println("Could not create JDA!");
				e.printStackTrace();
			}
		}
	}
}