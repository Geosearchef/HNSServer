package de.geosearchef.hnsserver.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum LocationType {
	HIDER("h"), SEEKER("s");//fake point, resource, ....

	private final String key;

	public static PlayerType fromKey(String k) {
		return Arrays.stream(PlayerType.values()).filter(t -> t.getKey().equals(k)).findAny().orElse(null);
	}
}
