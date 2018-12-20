package de.geosearchef.hnsserver;

import de.geosearchef.hnsserver.data.Player;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.geosearchef.hnsserver.HNSServer.gameService;

@Slf4j
public class PlayerService {

	private int ID_FACTORY = 0;

	private List<Player> players = new ArrayList<>();

	public PlayerService() {

	}

	public synchronized Player onConnect(String uuid, String name) {
		if(uuid.length() == 0 || name.length() == 0) {
			log.warn("Rejecting too short uuid or username");
			return null;
		}

		Optional<Player> alreadyConnectedPlayer = players.stream().filter(p -> p.getUuid() == uuid && p.getName().equals(name)).findFirst();
		if(alreadyConnectedPlayer.isPresent()) {
			log.warn("Username {}, uuid {} reconnected", uuid, name);
			gameService.leaveGame(alreadyConnectedPlayer.get());
			return alreadyConnectedPlayer.get();
		}

		Player player = new Player(ID_FACTORY++, uuid, name);

		players.add(player);

		return player;
	}


	public synchronized Optional<Player> getPlayer(int id) {
		return players.stream().filter(p -> p.getId() == id).findFirst();
	}
}
