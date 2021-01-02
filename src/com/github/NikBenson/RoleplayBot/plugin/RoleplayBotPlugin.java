package com.github.NikBenson.RoleplayBot.plugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class RoleplayBotPlugin extends ListenerAdapter {
    public RoleplayBotPlugin(JDA jda) {
        jda.addEventListener(this);
    }

    public void onLoad() {

    }
    public void onSave() {
    }
}
