package org.example.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.stream.Collectors;
import org.example.game.Game;
import org.example.game.commands.CreateRoundCommand;
import org.example.game.values.GameId;
import org.example.game.values.PlayerId;
import org.example.game.values.Round;
import org.example.gateway.GameDomainEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreateRoundUseCase extends UseCaseForCommand<CreateRoundCommand> {

  private final GameDomainEventRepository repository;

  public CreateRoundUseCase(GameDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<CreateRoundCommand> createRoundCommandMono) {
    return createRoundCommandMono.flatMapMany((command) ->
        repository
            .getEventsBy(command.getGameId().value()).collectList()
            .flatMapIterable(event -> {
              var game = Game.from(GameId.of(command.getGameId().value()), event);

              var players = command.getPlayers().stream()
                  .map(PlayerId::of)
                  .collect(Collectors.toSet());

              if (game.round() == null) {
                game.createRound(new Round(1, players), command.getTime());
              }

              game.round().incrementRound(players);

              return game.getUncommittedChanges();
            })
    );
  }
}
