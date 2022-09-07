package org.example.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.Game;
import org.example.game.commands.ChangeTimeCommand;
import org.example.game.values.GameId;
import org.example.gateway.GameDomainEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ChangeTimeUseCase extends UseCaseForCommand<ChangeTimeCommand> {

  private final GameDomainEventRepository repository;

  public ChangeTimeUseCase(GameDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<ChangeTimeCommand> changeTimeCommandMono) {
    return changeTimeCommandMono
        .flatMapMany(
            changeTimeCommand ->
                repository
                    .getEventsBy(changeTimeCommand.getGameId().value())
                    .collectList()
                    .flatMapIterable(event -> {
                      var game = Game.from(GameId.of(changeTimeCommand.getGameId().value()), event);

                      game.changeBoardTime(changeTimeCommand.getBoardId(),
                          changeTimeCommand.getTime());

                      return game.getUncommittedChanges();
                    }));
  }
}