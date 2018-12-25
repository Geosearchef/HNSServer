package de.geosearchef.hns.responses;

import de.geosearchef.hns.data.GameConfig;
import lombok.Data;

@Data
public class JoinResponse {
	private final String title;
	private final int key;
	private final GameConfig gameConfig;
}
