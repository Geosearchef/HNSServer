package de.geosearchef.hnsserver.data;

import lombok.Data;

@Data
public class Location {
	private final double latitude;
	private final double longitude;
	private final double radius;//precision or radius of item
	private final LocationType locationType;
}
