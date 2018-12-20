package de.geosearchef.hnsserver.responses;

import de.geosearchef.hnsserver.data.GameConfig;
import lombok.Data;

@Data
public class JoinResponse {
	private final String title;
	private final int key;
	private final GameConfig gameConfig;
}
