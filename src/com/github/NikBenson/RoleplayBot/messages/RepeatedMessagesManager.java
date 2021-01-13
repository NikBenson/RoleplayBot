package com.github.NikBenson.RoleplayBot.messages;

import com.github.NikBenson.RoleplayBot.commands.context.Context;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import com.github.NikBenson.RoleplayBot.configurations.ModulesManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RepeatedMessagesManager implements JSONConfigured {
	private static RepeatedMessagesManager instance;

	public static RepeatedMessagesManager setInstance(JDA jda) {
		if (instance == null) {
			instance = new RepeatedMessagesManager(jda);
		}

		return getInstance();
	}
	public static RepeatedMessagesManager getInstance()
	{
		return instance;
	}

	private final JDA JDA;

	List<RepeatedMessage> repeatedMessages = new LinkedList<>();

	private RepeatedMessagesManager(JDA jda) {
		JDA = jda;

		ModulesManager.registerModule("repeatedmessages", this);
	}

	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public File getConfigPath() {
		return new File(ConfigurationManager.getInstance().getConfigurationRootPath(), ConfigurationPaths.REPEATED_MESSAGES_DIRECTORY);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		stopOld();

		for(Object current : json.keySet()) {
			try {
				loadRepeatedMessage((JSONObject) json.get(current));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	private void stopOld() {
		for(RepeatedMessage repeatedMessage : repeatedMessages) {
			repeatedMessage.stop();
		}

		repeatedMessages.clear();
	}
	private void loadRepeatedMessage(JSONObject json) throws java.text.ParseException {
		TextChannel channel = JDA.getTextChannelById((Long) json.get("channel"));
		Date startAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) json.get("startAt"));
		long timeDelta = (long) json.get("timeDelta");

		String message = (String) json.get("message");
		JSONArray valuesJSON = (JSONArray) json.get("values");
		String[] values = new String[valuesJSON.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = (String) valuesJSON.get(i);
		}

		MessageFormatter<Context> messageFormatter = new MessageFormatter<>(message, values);
		repeatedMessages.add(new RepeatedMessage(channel, messageFormatter, startAt, timeDelta));
	}
}
