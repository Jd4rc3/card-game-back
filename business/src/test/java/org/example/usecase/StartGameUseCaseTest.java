package org.example.usecase;

import static org.mockito.Mockito.when;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Set;
import org.example.game.commands.StartGameCommand;
import org.example.game.events.BoardCreated;
import org.example.game.events.GameCreated;
import org.example.game.events.PlayerAdded;
import org.example.game.events.RoundCreated;
import org.example.game.values.Card;
import org.example.game.values.Deck;
import org.example.game.values.GameId;
import org.example.game.values.MasterCardId;
import org.example.game.values.PlayerId;
import org.example.gateway.GameDomainEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class StartGameUseCaseTest {

  @Mock
  private GameDomainEventRepository repository;

  @InjectMocks
  private StartGameUseCase useCase;

  @Test
  void startGame() {
    var gameId = GameId.of("1");
    var command = new StartGameCommand(gameId.value());

    when(repository.getEventsBy(command.getGameId())).thenReturn(history());

    StepVerifier.create(useCase.apply(Mono.just(command)))
        .expectNextMatches(event -> {
          var boardCreated = (BoardCreated) event;

          return command.getGameId().equals(boardCreated.aggregateRootId());
        })
        .expectNextMatches(event -> {
          var roundCreated = (RoundCreated) event;

          return roundCreated.getRound().value().number() == 1 && roundCreated.getTime() == 80;
        })
        .expectComplete()
        .verify();
  }

  private Flux<DomainEvent> history() {
    var card = new Card(MasterCardId.of("1"), true, true, 1);

    var cards = Set.of(card);

    var deck1 = new Deck(cards);
    var deck2 = new Deck(cards);
    var player1 = new PlayerAdded(PlayerId.of("p01"), "Player 1", deck1);
    var player2 = new PlayerAdded(PlayerId.of("p02"), "Player 2", deck2);

    return Flux.just(new GameCreated(PlayerId.of("p01")), player1, player2);
  }
}