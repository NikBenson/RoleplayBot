package com.github.NikBenson.RoleplayBot.roleplay;

import com.github.NikBenson.RoleplayBot.configurations.ConfigurationManager;
import com.github.NikBenson.RoleplayBot.configurations.ConfigurationPaths;
import com.github.NikBenson.RoleplayBot.configurations.JSONConfigured;
import com.github.NikBenson.RoleplayBot.roleplay.seasons.Season;
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

public class GameManager implements JSONConfigured {
	private static GameManager instance;

	public static GameManager getInstanceOrCreate() {
		if(instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	private Date startedAt;
	private long dayLengthInHours;
	private long day = 0;
	private Date currentDayStartedAt;
	private long refreshDelayInHours;

	private GameManager() {
		ConfigurationManager configurationManager = ConfigurationManager.getInstance();
		configurationManager.registerConfiguration(this);
		try {
			configurationManager.load(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void registerRefreshTimers() {
		Timer timer = new Timer();

		Date disabledUntil = Date.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().plusSeconds(5));

		long dayLengthInMilliseconds = dayLengthInHours *60*60*1000;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Date now = Date.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant());
				if(now.getTime() > disabledUntil.getTime()) {
					incrementDay();
				}
			}
		}, startedAt, dayLengthInMilliseconds);

		long refreshDelayInMilliseconds = refreshDelayInHours *60*60*1000;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Date now = Date.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant());
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
		Date now = Date.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant());

		long deltaInMillisecond = now.getTime() - startedAt.getTime();
		long deltaInHours = deltaInMillisecond / (1000 * 60 * 60);
		day = deltaInHours / dayLengthInHours;

		double hourOfDay = deltaInHours % dayLengthInHours;
		long millisecondOfDay = (long) (hourOfDay * (1000 * 60 * 60));
		long dayLengthInMilliseconds = dayLengthInHours * (1000 * 60 * 60);
		currentDayStartedAt = new Date(now.getTime() - (dayLengthInMilliseconds - millisecondOfDay));
	}
	private void setDay(long day) {
		this.day = day;

		currentDayStartedAt = Date.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant());
	}

	public long getDay() {
		return day;
	}

	public String getTime() {
		return getTime("HH:mm");
	}
	public String getTime(String pattern) {
		Date now = Date.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant());

		long deltaInMillisecond = now.getTime() - currentDayStartedAt.getTime();
		long dayLengthInMilliseconds = dayLengthInHours * (1000 * 60 *60);
		Date time = new Date(deltaInMillisecond * 24*60*60*1000 / dayLengthInMilliseconds - (1000*60*60));

		return new SimpleDateFormat(pattern).format(time);
	}

	@Override
	public JSONObject getJSON() {
		return null;
	}

	@Override
	public File getConfigPath() {
		return new File(ConfigurationManager.getInstance().getConfigurationRootPath(), ConfigurationPaths.GAME_CYCLE_FILE);
	}

	@Override
	public void loadFromJSON(JSONObject json) {
		Date now = Date.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant());

		try {
			startedAt = json.containsKey("startedAt")? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) json.get("startedAt")) : now;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dayLengthInHours = (long) json.getOrDefault("dayLength", 24l);
		refreshDelayInHours = (long) json.getOrDefault("refreshDelay", 6l);

		long passedUpdates = (now.getTime() - startedAt.getTime()) / (refreshDelayInHours*60*60*1000);
		Season.createSeasons((JSONArray) json.get("seasons"), passedUpdates);

		registerRefreshTimers();
		calculateDay();
	}
}
