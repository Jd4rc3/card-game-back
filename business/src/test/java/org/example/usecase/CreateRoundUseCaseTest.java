package org.example.usecase;

import static org.mockito.Mockito.when;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Set;
import org.example.game.commands.CreateRoundCommand;
import org.example.game.events.BoardCreated;
import org.example.game.events.GameCreated;
import org.example.game.events.RoundCreated;
import org.example.game.values.BoardId;
import org.example.game.values.GameId;
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
class CreateRoundUseCaseTest {

  @InjectMocks
  private CreateRoundUseCase useCase;

  @Mock
  private GameDomainEventRepository repository;

  @Test
  void createRound() {
    var gameId = GameId.of("1");
    var players = Set.of("p1", "p2");
    var command = new CreateRoundCommand(gameId, players);

    when(repository.getEventsBy(gameId.value())).thenReturn(history());

    StepVerifier.create(useCase.apply(Mono.just(command)))
        .expectNextMatches(event -> {
          var roundCreated = (RoundCreated) event;

          return "1".equals(roundCreated.aggregateRootId());
        })
        .expectComplete()
        .verify();

  }

  private Flux<DomainEvent> history() {
    var gameCreated = new GameCreated(PlayerId.of("p1"));
    var players = Set.of(PlayerId.of("p1"), PlayerId.of("p2"));
    var boardCreated = new BoardCreated(BoardId.of("b1"), players);

    return Flux.just(
        gameCreated,
        boardCreated
    );
  }
}