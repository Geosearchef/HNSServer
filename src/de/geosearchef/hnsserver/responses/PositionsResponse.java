package de.geosearchef.hnsserver.responses;

import de.geosearchef.hnsserver.data.Location;
import de.geosearchef.hnsserver.data.LocationSubject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class PositionsResponse {

	private final Map<LocationSubject, Location> locations;
	private final long nextRevealTime;
	private final long gameEndTime;

	private final long currentServerTime;
	//TODO: timers, game status
}
