package org.example.adapters.bus;

import org.example.ApplicationConfig;
import org.example.GsonEventSerializer;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventConsumer {

  private final GsonEventSerializer eventSerializer;

  private final ApplicationEventPublisher applicationEventPublisher;

  public RabbitMQEventConsumer(GsonEventSerializer eventSerializer,
      ApplicationEventPublisher eventPublisher) {

    this.eventSerializer = eventSerializer;
    this.applicationEventPublisher = eventPublisher;
  }

  @RabbitListener(
      bindings = @QueueBinding(
          value = @Queue(value = "game.handle", durable = "true"),
          exchange = @Exchange(value = ApplicationConfig.EXCHANGE, type = "topic"),
          key = "game.#"
      )
  )
  public void receiveMessage(Message<String> message) {
    var notification = Notification.from(message.getPayload());

    try {
      var event = eventSerializer.deserialize(notification.getBody(),
          Class.forName(notification.getType()));

      applicationEventPublisher.publishEvent(event);

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}