package org.example.cardgame.application.adapters.repo;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.application.generic.EventStoreRepository;
import org.example.gateway.GameDomainEventRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class MongoJuegoDomainEventRepository implements GameDomainEventRepository {

  private final EventStoreRepository repository;

  public MongoJuegoDomainEventRepository(EventStoreRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> getEventsBy(String id) {
    return repository.getEventsBy("game", id);
  }
}