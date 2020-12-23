package com.github.NikBenson.RoleplayBot.messages;

import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RepeatedMessage extends TimerTask {
	private final TextChannel channel;
	private final MessageFormatter message;

	private final Date startingTime;
	private final long period;

	public RepeatedMessage(@NotNull TextChannel channel, @NotNull MessageFormatter message, @NotNull Date startingTime, @NotNull long period) {
		this.channel = channel;
		this.message = message;
		this.startingTime = startingTime;
		this.period = period;

		startSchedule();
	}

	private void startSchedule() {
		Timer timer = new Timer();

		timer.schedule(
				this,
				startingTime,
				period
		);
	}

	@Override
	public void run() {
		channel.sendMessage(message.createMessage()).queue();
	}
}