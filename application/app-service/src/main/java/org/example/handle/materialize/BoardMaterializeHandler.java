package org.example.handle.materialize;

import co.com.sofka.domain.generic.DomainEvent;
import java.time.Instant;
import java.util.HashMap;
import org.example.game.events.BoardCreated;
import org.example.game.events.RoundCreated;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Configuration
public class BoardMaterializeHandler {

  private static final String COLLECTION_VIEW = "boardview";

  private final ReactiveMongoTemplate template;


  public BoardMaterializeHandler(ReactiveMongoTemplate template) {
    this.template = template;
  }

  @EventListener
  public void handleBoardCreated(BoardCreated boardCreated) {
    var data = new HashMap<>();
    var update = new Update();

    update.set("started", true);

    data.put("_id", boardCreated.aggregateRootId());
    data.put("players", boardCreated.getPlayerIds());
    data.put("round", new HashMap<>());
    data.put("date", Instant.now());

    template.updateFirst(getFilterByAggregateId(boardCreated), update, "gameview").block();
    template.save(data, COLLECTION_VIEW).block();
  }

  @EventListener
  public void handleRoundCreated(RoundCreated roundCreated) {
    var data = new Update();
    var round = roundCreated.getRound();

    data.set("date", Instant.now());
    data.set("round.playersAlive", round.value().players());
    data.set("round.number", round.value().number());
    data.set("round.isStarted", round.value().isStarted());

    template.updateFirst(getFilterByAggregateId(roundCreated), data, COLLECTION_VIEW)
        .block();
  }

  private Query getFilterByAggregateId(DomainEvent event) {
    return new Query(Criteria.where("_id").is(event.aggregateRootId()));
  }
}
