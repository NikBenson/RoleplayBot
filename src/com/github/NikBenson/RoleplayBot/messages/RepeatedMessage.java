package com.github.NikBenson.RoleplayBot.messages;

import com.github.NikBenson.RoleplayBot.commands.context.Context;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RepeatedMessage extends TimerTask {
	private final TextChannel channel;
	private final MessageFormatter<Context> message;

	private final Date startingTime;
	private final long period;

	private final Timer timer;

	public RepeatedMessage(@NotNull TextChannel channel, @NotNull MessageFormatter<Context> message, @NotNull Date startingTime, @NotNull long period) {
		this.channel = channel;
		this.message = message;
		this.startingTime = startingTime;
		this.period = period;
		timer = new Timer();

		startSchedule();
	}

	private void startSchedule() {
		timer.schedule(
				this,
				startingTime,
				period
		);
	}

	public void stop() {
		timer.cancel();
	}

	@Override
	public void run() {
		channel.sendMessage(message.createMessage(new Context())).queue();
	}
}
