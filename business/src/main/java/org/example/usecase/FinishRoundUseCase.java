package org.example.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.example.game.Game;
import org.example.game.commands.FinishRoundCommand;
import org.example.game.values.Card;
import org.example.game.values.GameId;
import org.example.game.values.PlayerId;
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
              TreeMap<Integer, String> orderedGame = new TreeMap<>((t1, t2) -> t2 - t1);

              Set<Card> cardsOnBoard = new HashSet<>();

              game.board().match().forEach((playerId, cards) -> {

                cards.stream()
                    .map(c -> c.value().power())
                    .reduce(Integer::sum)

                    .ifPresent(points -> {
                      orderedGame.put(points, playerId.value());
                      cardsOnBoard.addAll(cards);
                    });

              });

              var playersInGame = orderedGame.values()
                  .stream()
                  .map(PlayerId::of)
                  .collect(Collectors.toSet());

              var match = orderedGame.firstEntry();
              var winnerId = match.getValue();
              var points = match.getKey();

              game.asignCardsToPlayer(PlayerId.of(winnerId), points, cardsOnBoard);
              game.finishRound(game.board().identity(), playersInGame);

              return game.getUncommittedChanges();
            }));
  }
}
