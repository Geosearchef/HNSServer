package de.geosearchef.hns.data;

import de.geosearchef.hnsserver.Game;
import lombok.Data;

import java.util.Optional;

@Data
public class Player implements LocationSubject {

	private final int id;
	private final String uuid;
	private final String name;

	private long lastRequest = System.currentTimeMillis();
	private Optional<Game> game = Optional.empty();
	private PlayerType playerType;

	private String phantomName = "";

	@Override
	public String getDisplayedName() {
		if(playerType == PlayerType.PHANTOM) {
			return phantomName;
		} else {
			return name;
		}
	}
}
