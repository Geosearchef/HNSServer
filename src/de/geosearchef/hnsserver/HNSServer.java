package de.geosearchef.hnsserver;

public class HNSServer {

	static {
		System.setProperty("log4j.configurationFile", "log4j2.xml");
	}

	public static PlayerService playerService = new PlayerService();
	public static GameService gameService = new GameService();
	public static WebService webService = new WebService();


	public static void main(String args[]) {

	}
}
