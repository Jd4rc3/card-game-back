package org.example.game;

import co.com.sofka.domain.generic.EventChange;
import java.util.HashMap;
import org.example.game.entities.Player;
import org.example.game.events.GameCreated;
import org.example.game.events.PlayerAdded;

public class GameEventChange extends EventChange {

  public GameEventChange(Game game) {
    apply((GameCreated event) -> {
      game.Players = new HashMap<>();
      game.mainPlayer = event.getMainPlayer();
    });

    apply((PlayerAdded event) -> {
      game.Players.put(event.getIdentity(),
          new Player(event.getIdentity(), event.getAlias(), event.getDeck()));
    });
  }
}