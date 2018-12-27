package de.geosearchef.hns.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum PlayerType {
	HIDER("h"), SEEKER("s"), PHANTOM("p");

	private final String key;

	public static PlayerType fromKey(String k) {
		return Arrays.stream(PlayerType.values()).filter(t -> t.getKey().equals(k)).findAny().orElse(null);
	}
}
