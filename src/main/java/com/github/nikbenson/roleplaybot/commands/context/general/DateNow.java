package com.github.nikbenson.roleplaybot.commands.context.general;

import com.github.nikbenson.roleplaybot.commands.Command;
import com.github.nikbenson.roleplaybot.commands.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateNow extends Command<Context> {
	@Override
	public Class<Context> getContext() {
		return Context.class;
	}

	@Override
	public String getRegex() {
		return "date now( \"[\\. :-GyMwWDdFEuaHkKhmsSzZX]*\")?";
	}

	@Override
	public String execute(String command, Context context) {
		String pattern = "yyyy-MM-dd";

		if(!command.equals("date now")) {
			pattern = command.substring(10, command.length() - 2);
		}

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime currentDate = LocalDateTime.now();
		return dateTimeFormatter.format(currentDate);
	}
}
