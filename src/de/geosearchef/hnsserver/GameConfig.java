package de.geosearchef.hnsserver;

import lombok.Data;

@Data
public class GameConfig {
	private final GameOptions gameOptions;
	private final int locationUpdateInterval = Config.INSTANCE.getLOCATION_UPDATE_INTERVAL();
}
