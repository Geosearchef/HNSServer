package de.geosearchef.hnsserver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum PlayerType {
	HIDER("h"), SEEKER("s");

	private final String key;

	public static PlayerType fromKey(String k) {
		return Arrays.stream(PlayerType.values()).filter(t -> t.getKey().equals(k)).findAny().orElse(null);
	}
}