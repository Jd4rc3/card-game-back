package org.example.adapters.repo;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.gateway.GameDomainEventRepository;
import org.example.generic.EventStoreRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class MongoGameDomainEventRepository implements GameDomainEventRepository {

  private final EventStoreRepository repository;

  public MongoGameDomainEventRepository(EventStoreRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<DomainEvent> getEventsBy(String id) {
    return repository.getEventsBy("game", id);
  }

}