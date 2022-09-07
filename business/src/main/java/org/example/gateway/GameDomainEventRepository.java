package org.example.gateway;

import co.com.sofka.domain.generic.DomainEvent;
import reactor.core.publisher.Flux;

public interface GameDomainEventRepository {

  Flux<DomainEvent> getEventsBy(String id);
}
