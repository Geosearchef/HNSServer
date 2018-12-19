package de.geosearchef.hnsserver;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Game {
	private final int id;
	private final String title;
	private final int key;
	private final GameConfig gameConfig;

	private final List<Player> players = new ArrayList<>();

	private final Map<Player, Location> playerLocations = new HashMap<>();
}
