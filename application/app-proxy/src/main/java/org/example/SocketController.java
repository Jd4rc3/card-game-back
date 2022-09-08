package org.example;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ServerEndpoint("/retrieve/{correlationId}")
public class SocketController {

  private final Map<String, Map<String, Session>> sessions;
  @Autowired
  private GsonEventSerializer serialize;

  public SocketController() {
    Objects.requireNonNull(sessions = new ConcurrentHashMap<>());
  }

  @OnOpen
  public void onOpen(Session session, @PathParam("correlationId") String correlationId) {
    log.info("Connect by " + correlationId);
    var map = sessions.getOrDefault(correlationId, new HashMap<>());
    map.put(session.getId(), session);
    sessions.put(correlationId, map);
  }

  @OnClose
  public void onClose(Session session, @PathParam("correlationId") String correlationId) {
    sessions.get(correlationId).remove(session.getId());
    log.info("Disconnect by " + correlationId);

  }

  @OnError
  public void onError(Session session, @PathParam("correlationId") String correlationId,
      Throwable throwable) {
    sessions.get(correlationId).remove(session.getId());
    log.error(throwable.getMessage());

  }

  public void send(String correlationId, DomainEvent event) {

    var message = serialize.serialize(event);
    if (Objects.nonNull(correlationId) && sessions.containsKey(correlationId)) {
      log.info("send from " + correlationId);

      sessions.get(correlationId).values()
          .forEach(session -> {
            try {
              session.getAsyncRemote().sendText(message);
            } catch (RuntimeException e) {
              log.info(e.getMessage(), e);
            }
          });
    }
  }
}