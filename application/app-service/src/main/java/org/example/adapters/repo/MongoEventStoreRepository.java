package org.example.adapters.repo;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Comparator;
import org.example.generic.EventStoreRepository;
import org.example.generic.StoredEvent;
import org.example.generic.StoredEvent.EventSerializer;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MongoEventStoreRepository implements EventStoreRepository {

  private final ReactiveMongoTemplate template;

  private final StoredEvent.EventSerializer eventSerializer;

  public MongoEventStoreRepository(ReactiveMongoTemplate template,
      EventSerializer eventSerializer) {
    this.template = template;
    this.eventSerializer = eventSerializer;
  }

  @Override
  public Flux<DomainEvent> getEventsBy(String aggregateName, String aggregateRootId) {
    var query = new Query(Criteria.where("aggregateRootId").is(aggregateRootId));

    return template.find(query, DocumentEventStored.class, aggregateName)
        .sort(Comparator.comparing(event -> event.getStoredEvent().getOccurredOn()))
        .map(storeEvent -> storeEvent.getStoredEvent().deserializeEvent(eventSerializer));
  }

  @Override
  public Mono<Void> saveEvent(String aggregateName, String aggregateRootId,
      StoredEvent storedEvent) {
    var eventStored = new DocumentEventStored();
    eventStored.setAggregateRootId(aggregateRootId);
    eventStored.setStoredEvent(storedEvent);

    return template.save(eventStored, aggregateName).then();
  }
}