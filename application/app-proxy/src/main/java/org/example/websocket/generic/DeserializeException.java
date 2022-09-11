package org.example.websocket.generic;

public class DeserializeException extends RuntimeException {

  public DeserializeException(Throwable cause, String message) {
    super(message, cause);
  }
}
