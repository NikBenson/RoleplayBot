package com.github.NikBenson.RoleplayBot.json;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSONFileReader {
	public static JSONObject getJson(@NotNull File file) throws IOException, ParseException {
		String jsonString = Files.readString(Path.of(file.getPath()));

		return (JSONObject) new JSONParser().parse(jsonString);
	}
}
