package de.geosearchef.hnsserver;

import de.geosearchef.hns.data.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Game {
	private final int id;
	private final String title;
	private final int key;
	private final GameConfig gameConfig;

	private final List<Player> players = new ArrayList<>();

//	private final Map<LocationSubject, TimedLocation> locations = new HashMap<>();//TODO: only give out hider position at specific intervals
	private final List<TimedLocation> locations = new ArrayList<>();

	private final long gameEndTime;
	private long nextRevealTime;


	public Game(int id, String title, int key, GameConfig gameConfig) {
		this.id = id;
		this.title = title;
		this.key = key;
		this.gameConfig = gameConfig;


		this.gameEndTime = System.currentTimeMillis() + gameConfig.getGameOptions().getTotalTime();
		this.nextRevealTime = System.currentTimeMillis() + gameConfig.getGameOptions().getHiderRevealInterval();
	}

	//TODO: multihreading?
	public void addLocation(Location location, LocationSubject locationSubject) {
		TimedLocation newLocation = new TimedLocation(location, System.currentTimeMillis(), locationSubject);
		locations.add(newLocation);

		//TODO
		if(locationSubject instanceof Player && ((Player)locationSubject).getPlayerType() == PlayerType.SEEKER) {
			newLocation.setRevealed(true);
			//TODO: remove old locations
		}
	}

	//TODO: multihreading?
	public List<TimedLocation> getLocations() {
		processTimers();

		List<TimedLocation> result = new ArrayList<>();
		Collection<TimedLocation> revealedLocations = locations.stream().filter(TimedLocation::isRevealed).collect(Collectors.toList());
		revealedLocations.stream()
				.filter(location -> revealedLocations.stream().filter(l -> l.getLocationSubject() == location.getLocationSubject()).noneMatch(l -> l.getTimestamp() > location.getTimestamp()))
				.forEach(revealedLocations::add);
		return result;
	}

	public void processTimers() {
		if(gameEndTime < System.currentTimeMillis()) {
			locations.forEach(location -> location.setRevealed(true));
		}

		if(nextRevealTime < System.currentTimeMillis()) {
			locations.stream()
					.filter(location -> locations.stream().noneMatch(l -> l.getLocationSubject() == location.getLocationSubject() && l.getTimestamp() > location.getTimestamp()))
					.forEach(location -> location.setRevealed(true));

			nextRevealTime = System.currentTimeMillis() + gameConfig.getGameOptions().getHiderRevealInterval();
		}

		//IF locations are removed, copy list!
	}
}
