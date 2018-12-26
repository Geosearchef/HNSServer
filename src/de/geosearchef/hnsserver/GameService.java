package de.geosearchef.hnsserver;

import de.geosearchef.hns.data.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

//DO NOT CALL PLAYER SERVICE (deadlock)

@Slf4j
public class GameService {

	private int ID_FACTORY = 0;
	private Random random = new Random();

	private List<Game> games = new ArrayList<>();


	public synchronized Game createGame(Player player, GameOptions gameOptions, String title, PlayerType playerType) {
		leaveGame(player);

		log.info("{} is creating game", player.getId());

		if(! gameOptions.isValid()) {
			log.warn("Game options are invalid");
			return null;
		}

		GameConfig gameConfig = new GameConfig(gameOptions);
		Game game = new Game(ID_FACTORY++, title, getRandomKey(), gameConfig);

		game.getPlayers().add(player);
		player.setPlayerType(playerType);
		player.setGame(Optional.of(game));

		games.add(game);
		log.info("Created game {}, {}, key: {}", game.getId(), game.getTitle(), game.getKey());

		return game;
	}

	public synchronized Game joinGame(Player player, int key, PlayerType playerType) {
		leaveGame(player);

		log.info("{} is joining game with key {}", player.getId(), key);

		Optional<Game> game = games.stream().filter(g -> g.getKey() == key).findFirst();

		if(! game.isPresent()) {
			log.warn("No game with key {}", key);
		}

		game.get().getPlayers().add(player);
		player.setPlayerType(playerType);
		player.setGame(Optional.of(game.get()));

		return game.get();
	}

	public synchronized void leaveGame(Player player) {
		player.getGame().ifPresent(game -> {
			game.getPlayers().remove(player);
			player.setGame(Optional.empty());

			if(game.getPlayers().isEmpty()) {
				games.remove(game);
				log.info("Closed game {}, {} due to last player leaving", game.getId(), game.getTitle());
			}
		});
	}


	public synchronized void updateLocation(Player player, Location location) {
		log.info("Received location from player {}, {}", player.getId(), player.getName());
		player.getGame().ifPresent(game -> game.addLocation(location, player));
	}

	private synchronized int getRandomKey() {
		while(true) {
			int key = random.nextInt(900) + 100;

			if(games.stream().noneMatch(g -> g.getKey() == key)) {
				return key;
			}
		}
	}
}
