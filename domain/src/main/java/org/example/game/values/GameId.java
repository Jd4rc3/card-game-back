package org.example.game.values;

import co.com.sofka.domain.generic.Identity;

public class GameId extends Identity {

  public GameId(String uuid) {
    super(uuid);
  }

  public GameId() {
  }

  public static GameId of(String uuid) {
    return new GameId(uuid);
  }
}
