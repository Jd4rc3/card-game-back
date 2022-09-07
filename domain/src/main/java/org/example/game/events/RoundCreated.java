package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.values.Round;

public class RoundCreated extends DomainEvent {

  private final Round round;
  private final Integer time;

  public RoundCreated(Round round, Integer time) {
    super("game.RoundCreated");
    this.round = round;
    this.time = time;
  }

  public Round getRound() {
    return round;
  }

  public Integer getTime() {
    return time;
  }
}