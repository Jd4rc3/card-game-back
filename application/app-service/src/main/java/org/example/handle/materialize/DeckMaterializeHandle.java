package org.example.handle.materialize;

import java.time.Instant;
import java.util.HashMap;
import org.example.game.events.PlayerAdded;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class DeckMaterializeHandle {

  private static final String COLLECTION_VIEW = "deckview";

  private final ReactiveMongoTemplate template;

  public DeckMaterializeHandle(ReactiveMongoTemplate template) {
    this.template = template;
  }

  @EventListener
  public void handlePlayerAdded(PlayerAdded playerAdded) {
    var data = new HashMap<>();

    data.put("_id", playerAdded.aggregateRootId());
    data.put("date", Instant.now());
    data.put("uid", playerAdded.getIdentity().value());
    data.put("numberOfCards", playerAdded.getDeck().value().quantity());
    data.put("deck", playerAdded.getDeck().value().cards());

    template.save(data, COLLECTION_VIEW).block();
  }
}
