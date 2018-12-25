package de.geosearchef.hns.data;

import lombok.Data;

@Data
public class GameOptions {
	private final long totalTime;
	private final long hiderRevealInterval;

	public boolean isValid() {
		return totalTime > 1 && hiderRevealInterval > 1;
	}
}
