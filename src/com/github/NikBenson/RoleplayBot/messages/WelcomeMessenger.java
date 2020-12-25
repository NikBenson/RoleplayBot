package com.github.NikBenson.RoleplayBot.messages;

import com.github.NikBenson.RoleplayBot.messages.commands.context.user.UserContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WelcomeMessenger {
	private static MessageFormatter<UserContext> messageFormatter;
	private static boolean enabled = false;

	public static void sendTo(User user) {
		if(enabled) {
			user.openPrivateChannel().queue(channel -> {
				channel.sendMessage(messageFormatter.createMessage(new UserContext(user))).queue();
			});
		}
	}

	public static void init(JSONObject configuration) {
		String message = (String) configuration.get("message");
		JSONArray valuesJSON = (JSONArray) configuration.get("values");
		String[] values = new String[valuesJSON.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = (String) valuesJSON.get(i);
		}

		messageFormatter = new MessageFormatter<UserContext>(message, values);

		enabled = true;
	}

	private WelcomeMessenger() {}
}
