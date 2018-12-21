package de.geosearchef.hnsserver;

import com.google.gson.Gson;
import de.geosearchef.hnsserver.data.*;
import de.geosearchef.hnsserver.responses.JoinResponse;
import de.geosearchef.hnsserver.responses.PositionsResponse;
import de.geosearchef.hnsserver.responses.RegisterResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import spark.Request;

import java.util.ArrayList;
import java.util.Optional;

import static de.geosearchef.hnsserver.HNSServer.gameService;
import static de.geosearchef.hnsserver.HNSServer.playerService;
import static spark.Spark.*;

@Slf4j
public class WebService {

	private Gson gson = new Gson();

	public WebService() {
		port(Config.INSTANCE.getPORT());

		exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
			response.status(500);
		});

		get("/echo", (req, res) -> {
			return "echo";
		});

		post("/register", (req, res) -> {
			Player player = playerService.onConnect(req.queryParams("uuid"), req.queryParams("name"));
			if(player != null) {
				return new RegisterResponse(player.getId());
			} else {
				res.status(403);
				return res;
			}
		}, gson::toJson);

		post("/createGame", (req, res) -> {
			Player player = getPlayerFromRequest(req);
			if(player == null) {
				res.status(403);
				return res;
			}

			PlayerType playerType = PlayerType.fromKey(req.queryParams("playerType"));
			if(playerType == null) {
				res.status(403);
				return res;
			}

			var gameOptions = gson.fromJson(req.body(), GameOptions.class);

			Game game = gameService.createGame(player, gameOptions, req.queryParams("title"), playerType);

			if(game != null) {
				return new JoinResponse(game.getTitle(), game.getKey(), game.getGameConfig());
			} else {
				res.status(404);
				return res;
			}
		}, gson::toJson);

		post("/joinGame", (req, res) -> {
			Player player = getPlayerFromRequest(req);
			if(player == null) {
				res.status(403);
				return res;
			}

			PlayerType playerType = PlayerType.fromKey(req.queryParams("playerType"));
			if(playerType == null) {
				res.status(403);
				return res;
			}

			Game game = gameService.joinGame(player, Integer.parseInt(req.queryParams("key")), playerType);//TODO: is throwing a number format exception a problem?

			if(game != null) {
				return new JoinResponse(game.getTitle(), game.getKey(), game.getGameConfig());
			} else {
				res.status(404);
				return res;
			}
		}, gson::toJson);

		post("/leaveGame", (req, res) -> {
			Player player = getPlayerFromRequest(req);
			if(player == null) {
				res.status(403);
				return res;
			}

			gameService.leaveGame(player);

			res.status(200);
			return res;
		});


		post("/ownPosition", (req, res) -> {
			Player player = getPlayerFromRequest(req);
			if(player == null) {
				res.status(403);
				return null;
			}

			Location location = gson.fromJson(req.body(), Location.class);
			location.setLocationType(LocationType.fromKey(player.getPlayerType().getKey()));
			gameService.updateLocation(player, location);

			return null;
		});

		get("/positions", (req, res) -> {
			Player player = getPlayerFromRequest(req);
			if(player == null) {
				res.status(403);
				return res;
			}

			if(! player.getGame().isPresent()) {
				res.status(404);
				return res;
			}
			Game game = player.getGame().get();

			return new PositionsResponse(new ArrayList(game.getLocations()), game.getNextRevealTime(), game.getGameEndTime(), System.currentTimeMillis());
		}, gson::toJson);


		log.info("WebService started on port {}", Config.INSTANCE.getPORT());
	}





	private Player getPlayerFromRequest(Request req) {
		Optional<Player> player;
		int id;
		try {
			id = Integer.parseInt(req.queryParams("id"));
		} catch(NumberFormatException e) {
			log.error("Could not parse player id", e);
			return null;
		}

		player = playerService.getPlayer(id);

		if(! player.isPresent()) {
			log.error("No player found for id {}", id);
			return null;
		}

		if(! player.get().getUuid().equals(req.queryParams("uuid"))) {
			log.error("Player {} provided wrong uuid", id);
			return null;
		}

		player.get().setLastRequest(System.currentTimeMillis());
		return player.get();
	}
}