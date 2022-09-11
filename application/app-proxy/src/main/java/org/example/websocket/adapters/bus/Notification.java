package org.example.websocket.adapters.bus;

import com.google.gson.Gson;
import java.time.Instant;
import lombok.Getter;

@Getter
public class Notification {

  private final String type;

  private final String body;

  private final Instant instant;

  public Notification(String type, String body) {
    this.type = type;
    this.body = body;
    this.instant = Instant.now();
  }

  public Notification() {
    this(null, null);
  }

  public static Notification from(String aNotification) {
    return new Notification().deserialize(aNotification);
  }

  public Notification deserialize(String json) {
    return new Gson().fromJson(json, Notification.class);
  }

  public String serialize() {
    return new Gson().toJson(this);
  }
}