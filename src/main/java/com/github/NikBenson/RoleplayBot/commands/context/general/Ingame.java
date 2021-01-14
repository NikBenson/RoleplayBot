package com.github.NikBenson.RoleplayBot.commands.context.general;

import com.github.NikBenson.RoleplayBot.commands.Command;
import com.github.NikBenson.RoleplayBot.commands.context.Context;
import com.github.NikBenson.RoleplayBot.roleplay.GameManager;
import com.github.NikBenson.RoleplayBot.roleplay.seasons.Season;

public class Ingame extends Command<Context> {

	@Override
	public Class<Context> getContext() {
		return Context.class;
	}

	@Override
	public String getRegex() {
		return "ingame .*";
	}

	@Override
	public String execute(String command, Context context) {
		String args = command.substring(7);

		String special = executeSpecial(args);
		if(special != null) {
			return special;
		}

		String result = Season.getCurrent().get(args);

		if(result == null) return String.format("%s not found!", args);
		else return result;
	}

	private String executeSpecial(String args) {
		if(args.equals("day")) {
			return String.valueOf(GameManager.getInstanceOrCreate().getDay());
		} else if(args.matches("time( \"[. :-GyMwWDdFEuaHkKhmsSzZX]*\")?")) {
			String pattern = "HH:mm";

			if(!args.equals("time")) {
				pattern = args.substring(7, args.length() - 2);
			}

			return GameManager.getInstanceOrCreate().getTime(pattern);
		} else {
			return null;
		}
	}
}
