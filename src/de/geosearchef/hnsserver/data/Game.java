package de.geosearchef.hnsserver.data;

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

	private final Map<LocationSubject, Location> locations = new HashMap<>();//TODO: only give out hider position at specific intervals

	private final long gameEndTime;
	private final long nextRevealTime;


	public Game(int id, String title, int key, GameConfig gameConfig) {
		this.id = id;
		this.title = title;
		this.key = key;
		this.gameConfig = gameConfig;


		this.gameEndTime = System.currentTimeMillis() + gameConfig.getGameOptions().getTotalTime();
		this.nextRevealTime = System.currentTimeMillis() + gameConfig.getGameOptions().getHiderRevealInterval();
	}
}
