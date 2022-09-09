package org.example.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.Game;
import org.example.game.commands.StartGameCommand;
import org.example.game.values.GameId;
import org.example.game.values.Round;
import org.example.gateway.GameDomainEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class StartGameUseCase extends UseCaseForCommand<StartGameCommand> {

  private final GameDomainEventRepository repository;

  public StartGameUseCase(GameDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<StartGameCommand> startGameCommandMono) {
    return startGameCommandMono.flatMapMany(
        command -> repository.getEventsBy(command.getGameId())
            .collectList()
            .flatMapIterable(event -> {
              var game = Game.from(GameId.of(command.getGameId()), event);
              game.createBoard();
              var playersIds = game.players().keySet();
              var round = new Round(1, playersIds);

              game.createRound(round, 80);

              return game.getUncommittedChanges();
            }));
  }
}