package org.example.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.HashSet;
import java.util.Objects;
import org.example.game.Game;
import org.example.game.events.RoundFinished;
import org.example.game.values.GameId;
import org.example.gateway.GameDomainEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreateRoundUseCase extends UseCaseForEvent<RoundFinished> {

  private final GameDomainEventRepository repository;

  public CreateRoundUseCase(GameDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<RoundFinished> roundFinishedMono) {
    return roundFinishedMono.flatMapMany(event ->
        repository.getEventsBy(event.aggregateParentId())
            .collectList()
            .flatMapIterable(events -> {

              var game = Game.from(GameId.of(event.aggregateParentId()), events);
              var players = new HashSet<>(event.getPlayers());
              var round = game.round();

              if (Objects.isNull(round)) {
                throw new IllegalArgumentException("Should exist a round");
              }

              game.createRound(round.incrementRound(players), 60);

              return game.getUncommittedChanges();
            })
    );
  }
}