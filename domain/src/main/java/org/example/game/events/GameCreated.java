package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.values.PlayerId;

public class GameCreated extends
    DomainEvent {

  private final PlayerId mainPlayer;


  public GameCreated(PlayerId mainPlayer) {
    super("game.gamecreated");
    this.mainPlayer = mainPlayer;
  }

  public PlayerId getMainPlayer() {
    return mainPlayer;
  }
}
