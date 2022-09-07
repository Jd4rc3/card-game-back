package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.values.PlayerId;

public class GameFinished extends DomainEvent {

  private final PlayerId winner;

  private final String alias;

  public GameFinished(PlayerId playerId, String alias) {
    super("game.GameFinished");
    this.winner = playerId;
    this.alias = alias;
  }

  public PlayerId getWinner() {
    return winner;
  }

  public String getAlias() {
    return alias;
  }
}
