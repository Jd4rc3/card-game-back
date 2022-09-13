package org.example.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.stream.Collectors;
import org.example.game.Game;
import org.example.game.events.RoundFinished;
import org.example.game.values.GameId;
import org.example.gateway.GameDomainEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FindWinnerUseCase extends UseCaseForEvent<RoundFinished> {

  private final GameDomainEventRepository repository;

  public FindWinnerUseCase(GameDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<RoundFinished> roundFinishedMono) {
    return roundFinishedMono.flatMapMany(event ->
        repository.getEventsBy(event.aggregateParentId())
            .collectList()
            .flatMapIterable(events -> {
              var game = Game.from(GameId.of(event.aggregateParentId()), events);
              var alivePlayers = game.players().values().stream()
                  .filter(player -> player.deck().value().quantity() > 0)
                  .collect(Collectors.toList());

              if (alivePlayers.size() == 1) {
                var player = alivePlayers.get(0);
                game.finishGame(player.identity(), player.alias());

              } else if (event.getPlayers().size() == 1) {
                event.getPlayers().stream().findFirst()
                    .flatMap(playerId -> alivePlayers.stream()
                        .filter(player -> player.identity().value().equals(playerId.value()))
                        .findFirst())
                    .ifPresent(player -> game.finishGame(player.identity(), player.alias()));

              }

              return game.getUncommittedChanges();
            })
    );
  }
}
