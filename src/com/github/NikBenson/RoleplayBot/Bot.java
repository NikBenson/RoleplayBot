package com.github.NikBenson.RoleplayBot;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.general.*;
import com.github.NikBenson.RoleplayBot.commands.context.privateMessage.CancelCharacter;
import com.github.NikBenson.RoleplayBot.commands.context.privateMessage.CreateCharacter;
import com.github.NikBenson.RoleplayBot.commands.context.server.Shutdown;
import com.github.NikBenson.RoleplayBot.commands.context.server.Skill;
import com.github.NikBenson.RoleplayBot.commands.context.server.Storage;
import com.github.NikBenson.RoleplayBot.commands.context.user.PlayerName;
import com.github.NikBenson.RoleplayBot.json.ConfigLoader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.json.simple.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;

import static com.github.NikBenson.RoleplayBot.json.ConfigLoader.readJsonFromFile;

public class Bot {
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
		try {
			init(readJsonFromFile(new File(configurationDirectoryPath, "botinfo.json")));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		try {
			new ConfigLoader(jda, configurationDirectoryPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void init(JSONObject params) throws LoginException, InterruptedException {
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

		jda.awaitReady();
	}

}