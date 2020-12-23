package com.github.NikBenson.RoleplayBot.messages.commands;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateNow extends Command {
	@Override
	public String getRegex() {
		return "date now( \"[\\. :-GyMwWDdFEuaHkKhmsSzZX]*\")?";
	}

	@Override
	public String execute(String command) {
		String pattern = "yyyy-MM-dd";

		if(!command.equals("date now")) {
			pattern = command.substring(10, command.length() - 2);
		}

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime currentDate = LocalDateTime.now();
		return dateTimeFormatter.format(currentDate);
	}
}
