package com.github.NikBenson.RoleplayBot.messages.commands;

import java.util.Map;

public abstract class Context {
	public abstract Map<String, Object> getParams();
}
