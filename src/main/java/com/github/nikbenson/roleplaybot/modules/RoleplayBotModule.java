package com.github.nikbenson.roleplaybot.modules;

import net.dv8tion.jda.api.entities.Guild;

public interface RoleplayBotModule {
	boolean isActive(Guild guild);
	void load(Guild guild);
	void unload(Guild guild);
	Guild[] getLoaded();
}
