package de.geosearchef.hns.data;

import de.geosearchef.hnsserver.Config;
import lombok.Data;

@Data
public class GameConfig {
	private final GameOptions gameOptions;
	private final int locationUpdateInterval = Config.INSTANCE.getLOCATION_UPDATE_INTERVAL();
}
