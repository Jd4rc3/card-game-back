package org.example.generic;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Date;
import lombok.Data;

@Data
public class StoredEvent {

  private String eventBody;

  private Date occurredOn;

  private String typeName;

  public StoredEvent() {
  }

  public StoredEvent(String typeName, Date occurredOn, String eventBody) {
    this.typeName = typeName;
    this.occurredOn = occurredOn;
    this.eventBody = eventBody;
  }

  public static StoredEvent wrapEvent(DomainEvent domainEvent, EventSerializer eventSerializer) {
    return new StoredEvent(domainEvent.getClass().getCanonicalName(), new Date(),
        eventSerializer.serialize(domainEvent));
  }

  public interface EventSerializer {

    <T extends DomainEvent> T deserialize(String aSerialization, final Class<?> aType);

    String serialize(DomainEvent object);
  }
}