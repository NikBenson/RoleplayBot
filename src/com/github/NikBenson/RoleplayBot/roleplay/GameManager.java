package com.github.NikBenson.RoleplayBot.roleplay;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.github.NikBenson.RoleplayBot.json.JSONFileReader.getJson;

public class GameManager {
	private static GameManager instance;

	public static GameManager getInstance() {
		return instance;
	}

	public static GameManager setInstance(String configurationDirectoryPath) {
		instance = new GameManager(configurationDirectoryPath);

		return getInstance();
	}

	private final String configurationDirectoryPath;

	private Date startedAt;
	private long dayLengthInHours;
	private long day = 0;
	private Date currentDayStartedAt;
	private long refreshDelayInHours;

	private GameManager(String configurationDirectoryPath) {
		this.configurationDirectoryPath = configurationDirectoryPath;

		try {
			applyConfig();
		} catch (Exception e) {
			System.out.println("Could not load gameconfig.json!");
			e.printStackTrace();
		}
	}

	private void applyConfig() throws ParseException {
		JSONObject json = readConfigJson();

		System.out.println("read config done!");

		startedAt = json.containsKey("startedAt")? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) json.get("startedAt")) : Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
		dayLengthInHours = (long) json.getOrDefault("dayLength", 24l);
		refreshDelayInHours = (long) json.getOrDefault("refreshDelay", 6l);

		Season.createSeasons((JSONArray) json.get("seasons"));

		registerRefreshTimers();
		calculateDay();
	}
	private JSONObject readConfigJson() {
		File file = new File(configurationDirectoryPath, "gameconfig.json");
		if(file.exists()) {
			try {
				return getJson(file);
			} catch (Exception e) {
				System.out.println("Could not load gameconfig.json!");
				e.printStackTrace();
			}
		}
		return new JSONObject();
	}
	private void registerRefreshTimers() {
		Timer timer = new Timer();

		Date disabledUntil = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().plusSeconds(5));

		long dayLengthInMilliseconds = dayLengthInHours *60*60*1000;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
				if(now.getTime() > disabledUntil.getTime()) {
					incrementDay();
				}
			}
		}, startedAt, dayLengthInMilliseconds);

		long refreshDelayInMilliseconds = refreshDelayInHours *60*60*1000;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
				if(now.getTime() > disabledUntil.getTime()) {
					Season.update();
				}
			}
		}, startedAt, refreshDelayInMilliseconds);
	}
	private void incrementDay() {
		setDay(day + 1);
	}
	private void calculateDay() {
		Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

		long deltaInMillisecond = now.getTime() - startedAt.getTime();
		long deltaInHours = deltaInMillisecond / (1000 * 60 * 60);
		day = deltaInHours / dayLengthInHours;

		double hourOfDay = deltaInHours % dayLengthInHours;
		long millisecondOfDay = (long) (hourOfDay * (1000 * 60 * 60));
		long dayLengthInMilliseconds = dayLengthInHours * (1000 * 60 * 60);
		currentDayStartedAt = new Date(now.getTime() - (dayLengthInMilliseconds - millisecondOfDay));
		//TODO the line above is really buggy and no idea how to fix it. But will not matter after the first IG day.

		System.out.println(startedAt);
		System.out.printf("now: %tT, HOD: %f, DLM: %d, MOD: %d%n", now, hourOfDay, dayLengthInMilliseconds, millisecondOfDay);
		System.out.println(currentDayStartedAt);
	}
	private void setDay(long day) {
		this.day = day;

		currentDayStartedAt = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}

	public long getDay() {
		return day;
	}

	public String getTime() {
		return getTime("HH:mm");
	}
	public String getTime(String pattern) {
		Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

		long deltaInMillisecond = now.getTime() - currentDayStartedAt.getTime();
		long dayLengthInMilliseconds = dayLengthInHours * (1000 * 60 *60);
		Date time = new Date(deltaInMillisecond * 24*60*60*1000 / dayLengthInMilliseconds - (1000*60*60));

		return new SimpleDateFormat(pattern).format(time);
	}
}
