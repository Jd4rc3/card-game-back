package org.example.websocket.adapters.bus;

import org.example.websocket.ApplicationConfig;
import org.example.websocket.GsonEventSerializer;
import org.example.websocket.SocketController;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQEventConsumer {

  private final GsonEventSerializer eventSerializer;

  private final SocketController ws;


  public RabbitMQEventConsumer(GsonEventSerializer eventSerializer, SocketController ws) {
    this.eventSerializer = eventSerializer;
    this.ws = ws;
  }

  @RabbitListener(
      bindings = @QueueBinding(
          value = @Queue(value = "proxy.handles", durable = "true"),
          exchange = @Exchange(value = ApplicationConfig.EXCHANGE, type = "topic"),
          key = "game.#"
      )
  )
  public void receivedMessage(Message<String> message) {
    var notification = Notification.from(message.getPayload());

    try {
      var event = eventSerializer.deserialize(
          notification.getBody(), Class.forName(notification.getType())
      );

      ws.send(event.aggregateRootId(), event);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}

