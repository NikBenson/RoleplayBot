package com.github.NikBenson.RoleplayBot;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.general.*;
import com.github.NikBenson.RoleplayBot.commands.context.privateMessage.CancelCharacter;
import com.github.NikBenson.RoleplayBot.commands.context.privateMessage.CreateCharacter;
import com.github.NikBenson.RoleplayBot.commands.context.server.Shutdown;
import com.github.NikBenson.RoleplayBot.commands.context.server.Skill;
import com.github.NikBenson.RoleplayBot.commands.context.server.Storage;
import com.github.NikBenson.RoleplayBot.commands.context.user.PlayerName;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import com.github.NikBenson.RoleplayBot.serverCommands.MessageManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.simple.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;

import static com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager.readJsonFromFile;

public class Bot implements JSONConfigured {
	private JDA jda;

	private final String configurationDirectoryPath;

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

		try {
			loadFromJSON(readJsonFromFile(getConfigPath()));
			ConfigurationManager.setInstance(jda, configurationDirectoryPath).loadDefault();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		jda.addEventListener(new MessageManager());
	}

	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public File getConfigPath() {
		return new File(configurationDirectoryPath, ConfigurationPaths.BOT_INFO_FILE);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
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