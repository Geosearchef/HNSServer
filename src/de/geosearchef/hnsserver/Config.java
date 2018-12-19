package de.geosearchef.hnsserver;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

@Data
@Slf4j
public class Config {

	private final int LOCATION_UPDATE_INTERVAL;

	public static Config INSTANCE;
	private static final String CONFIG_FILE = "./config.cfg";
	static {
		Gson gson = new Gson();
		try {
			INSTANCE = gson.fromJson(Files.readAllLines(new File(CONFIG_FILE).toPath()).stream().collect(Collectors.joining(" ")), Config.class);
		} catch(IOException e) {
			log.error("Could not load config file.", e);
			System.exit(9765);
		}
	}
}
