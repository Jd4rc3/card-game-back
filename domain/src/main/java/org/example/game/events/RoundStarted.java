package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;

public class RoundStarted extends
    DomainEvent {

  public RoundStarted() {
    super("game.RoundStarted");
  }
}
