package org.example.handle.materialize;

import co.com.sofka.domain.generic.DomainEvent;
import java.time.Instant;
import java.util.HashMap;
import org.example.game.events.GameCreated;
import org.example.game.events.PlayerAdded;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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

  @EventListener
  public void handlePlayerAdded(PlayerAdded event) {
    var data = new Update();

    data.set("date", Instant.now());
    data.set(String.format("players.%s", event.getIdentity().value()), event.getAlias());

    data.inc("numberOfPlayers");

    template.updateFirst(getFilterByAggregateId(event), data, COLLECTION_VIEW)
        .block();
  }

  private Query getFilterByAggregateId(DomainEvent event) {
    return new Query(Criteria.where("_id").is(event.aggregateRootId()));
  }
}