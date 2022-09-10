package org.example.handle;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.function.Function;
import org.example.generic.EventBus;
import org.example.generic.EventStoreRepository;
import org.example.generic.StoredEvent;
import org.example.generic.StoredEvent.EventSerializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class IntegrationHandle implements Function<Flux<DomainEvent>, Mono<Void>> {

  private final EventStoreRepository repository;

  private final StoredEvent.EventSerializer eventSerializer;

  private final EventBus eventBus;

  public IntegrationHandle(EventStoreRepository repository, EventSerializer eventSerializer,
      EventBus eventBus) {
    this.repository = repository;
    this.eventSerializer = eventSerializer;
    this.eventBus = eventBus;
  }

  public Mono<Void> publishError(Throwable errorEvent) {
    return Mono.create((callback) -> {
      eventBus.publishError(errorEvent);
      callback.success();
    });
  }

  @Override
  public Mono<Void> apply(Flux<DomainEvent> domainEventFlux) {
    return domainEventFlux.flatMap(domainEvent -> {
      var stored = StoredEvent.wrapEvent(domainEvent, eventSerializer);

      return repository.saveEvent("game", domainEvent.aggregateRootId(), stored)
          .thenReturn(domainEvent);
    }).doOnNext(eventBus::publish).then();
  }

  @Override
  public <V> Function<V, Mono<Void>> compose(
      Function<? super V, ? extends Flux<DomainEvent>> before) {
    return Function.super.compose(before);
  }

  @Override
  public <V> Function<Flux<DomainEvent>, V> andThen(
      Function<? super Mono<Void>, ? extends V> after) {
    return Function.super.andThen(after);
  }
}
