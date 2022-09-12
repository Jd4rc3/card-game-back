package org.example.handle.materialize;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.domain.generic.Identity;
import java.time.Instant;
import java.util.HashMap;
import java.util.stream.Collectors;
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
    data.put("boardId", boardCreated.getBoardId().value());
    data.put("players",
        boardCreated.getPlayerIds().stream().map(Identity::value).collect(Collectors.toList()));

    data.put("cards", new HashMap<>());
    data.put("date", Instant.now());

    template.updateFirst(getFilterByAggregateId(boardCreated), update, "gameview").block();
    template.save(data, COLLECTION_VIEW).block();
  }

  @EventListener
  public void handleRoundCreated(RoundCreated roundCreated) {
    var board = new Update();
    var round = new HashMap<>();

    round.put("number", roundCreated.getRound().value().number());
    round.put("playersAlive",
        roundCreated.getRound().value().players().stream().map(Identity::value)
            .collect(Collectors.toList()));

    round.put("isStarted", roundCreated.getRound().value().isStarted());
    round.put("time", roundCreated.getTime());

    board.set("round", round);

    template.updateFirst(getFilterByAggregateId(roundCreated), board, COLLECTION_VIEW)
        .block();
  }

  private Query getFilterByAggregateId(DomainEvent event) {
    return new Query(Criteria.where("_id").is(event.aggregateRootId()));
  }
}
