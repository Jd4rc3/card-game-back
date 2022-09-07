package org.example.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Set;
import org.example.game.Game;
import org.example.game.commands.PutCardOnBoardCommand;
import org.example.game.values.Card;
import org.example.game.values.GameId;
import org.example.game.values.PlayerId;
import org.example.gateway.GameDomainEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PutCardsOnBoardUseCase extends UseCaseForCommand<PutCardOnBoardCommand> {

  private final GameDomainEventRepository repository;

  public PutCardsOnBoardUseCase(GameDomainEventRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<PutCardOnBoardCommand> putCardOnBoardCommandMono) {
    return putCardOnBoardCommandMono.flatMapMany(
        command -> repository
            .getEventsBy(command.getGameId())
            .collectList()
            .flatMapIterable(
                events -> {
                  var game = Game.from(GameId.of(command.getGameId()), events);
                  var boardId = game.board().identity();
                  var playerId = PlayerId.of(command.getPlayerId());
                  var cardsOfThePlayer = game.players().get(playerId).deck().value().cards();
                  var selectedCard = selectCard(command.getCardId(), cardsOfThePlayer);

                  var amount = (long) game.board().match()
                      .get(playerId).size();
                  if (amount > 1) {
                    throw new IllegalArgumentException(
                        "You can't put more than 1 card on the board");
                  }
                  game.putCardOnBoard(boardId, playerId, selectedCard);
                  return game.getUncommittedChanges();
                }));
  }

  private Card selectCard(String cardId, Set<Card> cardsOfThePlayer) {
    return cardsOfThePlayer
        .stream()
        .filter(c -> c.value().cardId().value().equals(cardId))
        .findFirst()
        .orElseThrow();
  }
}