package org.example.usecase;


import static org.mockito.Mockito.when;

import java.util.HashMap;
import org.example.game.commands.CreateGameCommand;
import org.example.game.events.GameCreated;
import org.example.game.events.PlayerAdded;
import org.example.game.values.GameId;
import org.example.gateway.CardsListService;
import org.example.gateway.model.MasterCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CreateGameUseCaseTest {

  @Mock
  private CardsListService service;

  @InjectMocks
  private CreateGameUseCase useCase;

  @Test
  void createGame() {
    var gameId = GameId.of("g01");
    var players = new HashMap<String, String>();
    players.put("p01", "Player #1");
    players.put("p02", "Player #2");

    var command = new CreateGameCommand(gameId.value(), players, "p01");

    when(service.getCardsFromMarvel()).thenReturn(cardsGame());

    StepVerifier.create(useCase.apply(Mono.just(command)))
        .expectNextMatches(event -> {
          var gameCreated = (GameCreated) event;

          return "g01".equals(gameCreated.aggregateRootId())
              && "p01".equals(gameCreated.getMainPlayer().value());
        })
        .expectNextMatches(event -> {
          var playerAdded = (PlayerAdded) event;

          return "p01".equals(playerAdded.getIdentity().value())
              && "Player #1".equals(playerAdded.getAlias());
        })
        .expectNextMatches(event -> {
          var playerAdded = (PlayerAdded) event;

          return "p02".equals(playerAdded.getIdentity().value())
              && "Player #2".equals(playerAdded.getAlias());
        })
        .expectComplete()
        .verify();
  }

  private Flux<MasterCard> cardsGame() {
    return Flux.just(
        new MasterCard("card-001", "test #1"),
        new MasterCard("card-002", "test #2"),
        new MasterCard("card-003", "test #3"),
        new MasterCard("card-004", "test #5"),
        new MasterCard("card-006", "test #6"),
        new MasterCard("card-007", "test #7"),
        new MasterCard("card-008", "test #8"),
        new MasterCard("card-009", "test #9"),
        new MasterCard("card-010", "test #10"),
        new MasterCard("card-011", "test #11")
    );
  }
}