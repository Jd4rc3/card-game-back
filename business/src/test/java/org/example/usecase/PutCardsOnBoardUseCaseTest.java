package org.example.usecase;

import static org.mockito.Mockito.when;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Set;
import org.example.game.commands.PutCardOnBoardCommand;
import org.example.game.events.BoardCreated;
import org.example.game.events.CardPutOnBoard;
import org.example.game.events.CardRemovedFromDeck;
import org.example.game.events.GameCreated;
import org.example.game.events.PlayerAdded;
import org.example.game.events.RoundCreated;
import org.example.game.events.RoundStarted;
import org.example.game.values.BoardId;
import org.example.game.values.Card;
import org.example.game.values.Deck;
import org.example.game.values.MasterCardId;
import org.example.game.values.PlayerId;
import org.example.game.values.Round;
import org.example.gateway.GameDomainEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(MockitoExtension.class)
class PutCardsOnBoardUseCaseTest {

  @Mock
  private GameDomainEventRepository repository;

  @InjectMocks
  private PutCardsOnBoardUseCase useCase;

  @Test
  void putCard() {
    var playerId = "p01";
    var cardId = "1";
    var gameId = "g1";

    var command = new PutCardOnBoardCommand(playerId, cardId, gameId);
    command.setCardId(cardId);
    command.setGameId(gameId);
    command.setPlayerId(playerId);

    when(repository.getEventsBy(command.getGameId())).thenReturn(history());

    StepVerifier.create(useCase.apply(Mono.just(command)))
        .expectNextMatches(event -> {
          var cardPutOnBoard = (CardPutOnBoard) event;
          Assertions.assertEquals("p01", cardPutOnBoard.getPlayerId().value());

          return command.getCardId().equals(cardPutOnBoard.getCard().value().cardId().value());
        })
        .expectNextMatches(event -> {
          var cardPutOnBoard = (CardRemovedFromDeck) event;
          Assertions.assertEquals("p01", cardPutOnBoard.getPlayerId().value());

          return command.getCardId().equals(cardPutOnBoard.getCard().value().cardId().value());
        })
        .expectComplete()
        .verify();
  }

  private Flux<DomainEvent> history() {
    var carta = new Card(MasterCardId.of("1"), true, true);
    var cards = Set.of(carta);
    var deck1 = new Deck(cards);
    var deck2 = new Deck(cards);

    var playerAdded = new PlayerAdded(PlayerId.of("p01"), "Player 1", deck1);
    var player2 = new PlayerAdded(PlayerId.of("p02"), "Player 2", deck2);
    var playerIds = Set.of(playerAdded.getIdentity(), player2.getIdentity());

    var round = new Round(1, playerIds);

    return Flux.just(
        new GameCreated(PlayerId.of("g1")),
        playerAdded,
        new BoardCreated(BoardId.of("b1"), playerIds),
        new RoundCreated(round, 80),
        new RoundStarted()
    );
  }
}