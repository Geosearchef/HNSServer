package de.geosearchef.hnsserver;

import lombok.Data;

@Data
public class GameOptions {
	private final int totalTime;
	private final int hiderRevealInterval;
	private final int seekerRevealInterval;

	public boolean isValid() {
		return totalTime > 1 && hiderRevealInterval > 1 && seekerRevealInterval > 1;
	}
}
