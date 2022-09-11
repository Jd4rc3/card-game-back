package org.example.handle.materialize;

import java.time.Instant;
import java.util.HashMap;
import org.example.game.events.GameCreated;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class GameMaterializeHandle {

  private static final String COLLECTION_VIEW = "gameview";

  private final ReactiveMongoTemplate template;


  public GameMaterializeHandle(ReactiveMongoTemplate template) {
    this.template = template;
  }

  @EventListener
  public void handleCreatedGames(GameCreated gameCreated) {
    var data = new HashMap<>();

    data.put("_id", gameCreated.aggregateRootId());
    data.put("date", Instant.now());
    data.put("uid", gameCreated.getMainPlayer().value());
    data.put("started", false);
    data.put("finished", false);
    data.put("numberOfPlayers", 0);
    data.put("players", new HashMap<>());

    template.save(data, COLLECTION_VIEW).block();
  }
}
