package de.geosearchef.hnsserver;

import lombok.Data;

import java.util.Optional;

@Data
public class Player {

	private final int id;
	private final String uuid;
	private final String name;

	private long lastRequest = System.currentTimeMillis();
	private Optional<Game> game = Optional.empty();
	private PlayerType playerType;
}
