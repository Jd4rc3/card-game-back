package org.example.adapters.bus;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.ApplicationConfig;
import org.example.GsonEventSerializer;
import org.example.generic.EventBus;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQEventBus implements EventBus {

  private final RabbitTemplate rabbitTemplate;

  private final GsonEventSerializer eventSerializer;

  public RabbitMQEventBus(RabbitTemplate rabbitTemplate, GsonEventSerializer eventSerializer) {
    this.rabbitTemplate = rabbitTemplate;
    this.eventSerializer = eventSerializer;
  }

  @Override
  public void publish(DomainEvent event) {
    var notification = new Notification(
        event.getClass().getTypeName(),
        eventSerializer.serialize(event)
    );

    rabbitTemplate.convertAndSend(
        ApplicationConfig.EXCHANGE, event.type, notification.serialize().getBytes()
    );
  }

  @Override
  public void publishError(Throwable errorEvent) {
    var event = new ErrorEvent(errorEvent.getClass().getTypeName(), errorEvent.getMessage());
    var notification = new Notification(
        event.getClass().getTypeName(),
        eventSerializer.serialize(event)
    );

    rabbitTemplate.convertAndSend(ApplicationConfig.EXCHANGE, event.type,
        notification.serialize().getBytes());
  }
}