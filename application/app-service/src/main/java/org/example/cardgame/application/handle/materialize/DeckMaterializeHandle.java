package org.example.cardgame.application.handle.materialize;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.events.PlayerAdded;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Configuration
public class DeckMaterializeHandle {

  private static final String COLLECTION_VIEW = "mazoview";

  private final ReactiveMongoTemplate template;

  public DeckMaterializeHandle(ReactiveMongoTemplate template) {
    this.template = template;
  }


  @EventListener
  public void handleJugadorAgregado(PlayerAdded event) {
    event.getDeck();
  }

  private Query getFilterByAggregateId(DomainEvent event) {
    return new Query(
        Criteria.where("_id").is(event.aggregateRootId())
    );
  }
}