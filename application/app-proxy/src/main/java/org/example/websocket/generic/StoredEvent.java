package org.example.websocket.generic;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Date;
import lombok.Data;

@Data
public class StoredEvent {

  private String eventBody;

  private Date ocurredOn;

  private String typeName;

  public StoredEvent() {
  }

  public StoredEvent(String eventBody, Date ocurredOn, String typeName) {
    this.eventBody = eventBody;
    this.ocurredOn = ocurredOn;
    this.typeName = typeName;
  }

  public static StoredEvent wrapEvent(DomainEvent domainEvent, EventSerializer eventSerializer) {
    return new StoredEvent(domainEvent.getClass().getCanonicalName(),
        new Date(),
        eventSerializer.serialize(domainEvent)
    );
  }

  public DomainEvent deserializeEvent(EventSerializer eventSerializer) {
    try {
      return eventSerializer.deserialize(this.getEventBody(), Class.forName(this.getTypeName()));
    } catch (ClassNotFoundException e) {
      throw new DeserializeException(e.getCause(), e.getMessage());
    }
  }

  public interface EventSerializer {

    <T extends DomainEvent> T deserialize(String aSerialization, final Class<?> aType);

    String serialize(DomainEvent object);
  }
}
