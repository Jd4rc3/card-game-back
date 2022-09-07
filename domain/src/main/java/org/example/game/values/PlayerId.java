package org.example.game.values;

import co.com.sofka.domain.generic.Identity;

public class PlayerId extends Identity {

  public PlayerId(String id) {
    super(id);
  }

  public PlayerId() {
  }

  public static PlayerId of(String id) {
    return new PlayerId(id);
  }
}
