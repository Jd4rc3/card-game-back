package org.example.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.Game;
import org.example.game.commands.FinishRoundCommand;
import org.example.game.values.GameId;
import org.example.gateway.GameDomainEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FinishRoundUseCase extends UseCaseForCommand<FinishRoundCommand> {

  private final GameDomainEventRepository repository;

  public FinishRoundUseCase(GameDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<FinishRoundCommand> finishRoundCommandMono) {
    return finishRoundCommandMono.flatMapMany(
        command -> repository.getEventsBy(command.getGameId().value())
            .collectList()
            .flatMapIterable(event -> {
              var game = Game.from(GameId.of(command.getGameId().value()), event);
              game.finishRound(command.getBoardId(), command.getPlayers());

              return game.getUncommittedChanges();
            }));
  }
}
