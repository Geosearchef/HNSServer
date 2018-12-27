package de.geosearchef.hns.data;

import lombok.Data;

@Data
public class TimedLocation {
	private final Location location;
	private final long timestamp;
	private final String name;
	private final boolean specialColor;
	private transient final LocationSubject locationSubject;
	private boolean revealed = false;

	public TimedLocation(Location location, long timestamp, LocationSubject locationSubject) {
		this.location = location;
		this.timestamp = timestamp;
		this.locationSubject = locationSubject;
		this.name = locationSubject.getDisplayedName();
		this.specialColor = locationSubject instanceof Player && ((Player)locationSubject).getPlayerType() == PlayerType.HIDER;
	}
}
