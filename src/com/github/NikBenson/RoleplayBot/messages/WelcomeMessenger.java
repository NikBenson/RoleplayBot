package com.github.NikBenson.RoleplayBot.messages;

import com.github.NikBenson.RoleplayBot.messages.commands.context.user.UserContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WelcomeMessenger {
	private static MessageFormatter<UserContext> messageFormatter;
	private static JDA jda;

	public static void sendTo(User user) {
		user.openPrivateChannel().queue(channel -> {
			channel.sendMessage(messageFormatter.createMessage(new UserContext(user))).queue();
		});
	}

	public static void init(JDA jda, JSONObject configuration) {
		WelcomeMessenger.jda = jda;


		String message = (String) configuration.get("message");
		JSONArray valuesJSON = (JSONArray) configuration.get("values");
		String[] values = new String[valuesJSON.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = (String) valuesJSON.get(i);
		}

		messageFormatter = new MessageFormatter<UserContext>(message, values);
	}

	private WelcomeMessenger() {}
}
