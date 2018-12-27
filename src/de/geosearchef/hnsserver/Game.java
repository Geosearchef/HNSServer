package de.geosearchef.hnsserver;

import de.geosearchef.hns.data.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Game {
	private final int id;
	private final String title;
	private final int key;
	private final GameConfig gameConfig;

	private final List<Player> players = new ArrayList<>();

//	private final Map<LocationSubject, TimedLocation> locations = new HashMap<>();//TODO: only give out hider position at specific intervals
	private final List<TimedLocation> locations = new ArrayList<>();

	private final long gameEndTime;
	private long nextRevealTime;


	public Game(int id, String title, int key, GameConfig gameConfig) {
		this.id = id;
		this.title = title;
		this.key = key;
		this.gameConfig = gameConfig;


		this.gameEndTime = System.currentTimeMillis() + gameConfig.getGameOptions().getTotalTime();
		this.nextRevealTime = System.currentTimeMillis() + gameConfig.getGameOptions().getHiderRevealInterval();
	}

	//TODO: multihreading?
	public void addLocation(Location location, LocationSubject locationSubject) {
		TimedLocation newLocation = new TimedLocation(location, System.currentTimeMillis(), locationSubject);
		locations.add(newLocation);

		//TODO
		if(locationSubject instanceof Player && ((Player)locationSubject).getPlayerType() == PlayerType.SEEKER) {
			newLocation.setRevealed(true);
			//TODO: remove old locations
		}
	}

	//TODO: multihreading?
	public List<TimedLocation> getLocations() {
		processTimers();

		List<TimedLocation> result = new ArrayList<>();
		Collection<TimedLocation> revealedLocations = locations.stream().filter(TimedLocation::isRevealed).collect(Collectors.toList());
		revealedLocations.stream()
				.filter(location -> revealedLocations.stream().filter(l -> l.getLocationSubject() == location.getLocationSubject()).noneMatch(l -> l.getTimestamp() > location.getTimestamp()))
				.forEach(result::add);
		/* TODO:
		java.util.ConcurrentModificationException
	at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1388)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:481)
	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:471)
	at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
	at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:174)
	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
	at de.geosearchef.hnsserver.Game.getLocations(Game.java:58)
	at de.geosearchef.hnsserver.WebService.lambda$new$7(WebService.java:137)
	at spark.ResponseTransformerRouteImpl$1.handle(ResponseTransformerRouteImpl.java:47)
	at spark.http.matching.Routes.execute(Routes.java:61)
	at spark.http.matching.MatcherFilter.doFilter(MatcherFilter.java:130)
	at spark.embeddedserver.jetty.JettyHandler.doHandle(JettyHandler.java:50)
	at org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:1568)
	at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:141)
	at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:132)
	at org.eclipse.jetty.server.Server.handle(Server.java:530)
	at org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:347)
	at org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:256)
	at org.eclipse.jetty.io.AbstractConnection$ReadCallback.succeeded(AbstractConnection.java:279)
	at org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:102)
	at org.eclipse.jetty.io.ChannelEndPoint$2.run(ChannelEndPoint.java:124)
	at org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.doProduce(EatWhatYouKill.java:247)
	at org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.produce(EatWhatYouKill.java:140)
	at org.eclipse.jetty.util.thread.strategy.EatWhatYouKill.run(EatWhatYouKill.java:131)
	at org.eclipse.jetty.util.thread.ReservedThreadExecutor$ReservedThread.run(ReservedThreadExecutor.java:382)
	at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:708)
	at org.eclipse.jetty.util.thread.QueuedThreadPool$2.run(QueuedThreadPool.java:626)
	at java.lang.Thread.run(Thread.java:748)
		 */
		return result;
	}

	public void processTimers() {
		if(gameEndTime < System.currentTimeMillis()) {
			locations.forEach(location -> location.setRevealed(true));
		}

		if(nextRevealTime < System.currentTimeMillis()) {
			locations.stream()
					.filter(location -> locations.stream().noneMatch(l -> l.getLocationSubject() == location.getLocationSubject() && l.getTimestamp() > location.getTimestamp()))
					.forEach(location -> location.setRevealed(true));

			nextRevealTime = System.currentTimeMillis() + gameConfig.getGameOptions().getHiderRevealInterval();
		}

		//IF locations are removed, copy list!
	}
}
