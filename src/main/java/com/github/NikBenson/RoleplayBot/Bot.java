package com.github.NikBenson.RoleplayBot;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.GuildMessageContext;
import com.github.NikBenson.RoleplayBot.commands.context.PrivateMessageContext;
import com.github.NikBenson.RoleplayBot.commands.context.cli.ReloadModules;
import com.github.NikBenson.RoleplayBot.commands.context.cli.Shutdown;
import com.github.NikBenson.RoleplayBot.commands.context.general.DateNow;
import com.github.NikBenson.RoleplayBot.commands.context.guild.ModuleLoad;
import com.github.NikBenson.RoleplayBot.commands.context.guild.ModuleUnload;
import com.github.NikBenson.RoleplayBot.commands.context.guild.Reload;
import com.github.NikBenson.RoleplayBot.commands.context.guild.Save;
import com.github.NikBenson.RoleplayBot.commands.context.user.PlayerName;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.messages.InputManager;
import com.github.NikBenson.RoleplayBot.modules.ModuleConfig;
import com.github.NikBenson.RoleplayBot.modules.ModuleLoader;
import com.github.NikBenson.RoleplayBot.modules.ModulesManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

import static com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager.readJSONFromFile;

public class Bot extends ListenerAdapter {
	private static Bot instance;

	public static void main(String[] args) {
		if(instance == null) {
			registerCommands();

			instance = new Bot(args[0]);
		}
	}
	private static void registerCommands() {
		Command.register(new DateNow(),
				new PlayerName(),
				new Shutdown(),
				new Save(),
				new Reload(),
				new ReloadModules(),
				new ModuleLoad(),
				new ModuleUnload()
		);

		try {
			new GuildMessageContext(null);
			new PrivateMessageContext(null);
		} catch (Exception ignored) {}
	}

	public static JDA getJDA() {
		return instance.jda;
	}

	private JDA jda;

	private final String configurationDirectoryPath;

	private Bot(String configurationDirectoryPath) {
		this.configurationDirectoryPath = configurationDirectoryPath;

		try {
			loadFromJSON((JSONObject) readJSONFromFile(getConfigPath()));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		jda.addEventListener(this);
	}

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		try {
			ConfigurationManager.setInstance(configurationDirectoryPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		new ModuleLoader(new File(configurationDirectoryPath, ConfigurationPaths.MODULES_DIRECTORY));
		ModuleConfig.loadModules();
	}

	public File getConfigPath() {
		return new File(configurationDirectoryPath, ConfigurationPaths.BOT_INFO_FILE);
	}

	public void loadFromJSON(JSONObject json) {
		if(jda == null) {
			JDABuilder builder = JDABuilder.createDefault((String) json.get("token"));

			builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
			builder.setBulkDeleteSplittingEnabled(false);
			builder.setCompression(Compression.NONE);
			builder.enableIntents(GatewayIntent.GUILD_MEMBERS);

			builder.addEventListeners(new InputManager((String) json.getOrDefault("prefix", "!")));

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