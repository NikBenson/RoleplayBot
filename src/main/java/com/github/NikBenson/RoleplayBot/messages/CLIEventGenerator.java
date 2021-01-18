package com.github.NikBenson.RoleplayBot.messages;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class CLIEventGenerator extends Thread {
	private static CLIEventGenerator instance;

	private static @NotNull CLIEventGenerator getInstanceOrCreate() {
		if(instance == null) {
			instance = new CLIEventGenerator();
		}

		return instance;
	}

	public static void addListener(CLIEventListener listener) {
		getInstanceOrCreate().listeners.add(listener);
	}
	public static void removeListener(CLIEventListener listener) {
		getInstanceOrCreate().listeners.remove(listener);
	}

	private final Set<CLIEventListener> listeners = new HashSet<>();

	private final BufferedReader CLI_READER = new BufferedReader(new InputStreamReader(System.in));

	private CLIEventGenerator() {
		super();

		start();
	}

	@Override
	public void run() {
		while (true) {
			String line = tryReadLine();

			if(line != null) {
				runEvent(line);
			}
		}
	}
	private String tryReadLine() {
		try {
			return CLI_READER.readLine();
		} catch (IOException e) {
			return null;
		}
	}
	private void runEvent(String line) {
		for (CLIEventListener listener : listeners) {
			listener.onLineEntered(line);
		}
	}
}
