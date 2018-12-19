package de.geosearchef.hnsserver.responses;

import de.geosearchef.hnsserver.GameConfig;
import lombok.Data;

@Data
public class JoinResponse {
	private final String title;
	private final int key;
	private final GameConfig gameConfig;
}
