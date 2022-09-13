package org.example.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.game.Game;
import org.example.game.commands.FinishRoundCommand;
import org.example.game.events.RoundStarted;
import org.example.game.values.GameId;
import org.example.gateway.GameDomainEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class StartCountDownUseCase extends UseCaseForEvent<RoundStarted> {

  private final GameDomainEventRepository repository;

  private final FinishRoundUseCase finishRoundUseCase;

  public StartCountDownUseCase(GameDomainEventRepository repository,
      FinishRoundUseCase finishRoundUseCase) {
    this.repository = repository;
    this.finishRoundUseCase = finishRoundUseCase;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<RoundStarted> roundStartedMono) {
    AtomicInteger accumulator = new AtomicInteger(0);

    var finishRoundCommand = new FinishRoundCommand();

    return roundStartedMono.flatMapMany((event) -> repository
        .getEventsBy(event.aggregateRootId())
        .collectList()
        .flatMapMany(events -> {

          var game = Game.from(GameId.of(event.aggregateRootId()), events);
          finishRoundCommand.setGameId(event.aggregateRootId());
          var time = game.board().time();
          var boardId = game.board().identity();

          return Flux.interval(Duration.ofSeconds(1))
              .onBackpressureBuffer(1)
              .take(time)
              .flatMap(t -> {

                game.markChangesAsCommitted();
                var newTime = time - accumulator.getAndIncrement();
                game.changeBoardTime(boardId, newTime);

                if (newTime == 1) {
                  return finishRoundUseCase.apply(Mono.just(finishRoundCommand))
                      .mergeWith(Flux.fromIterable(game.getUncommittedChanges()));
                }

                return Flux.fromIterable(game.getUncommittedChanges());

              });
        }));
  }
}
