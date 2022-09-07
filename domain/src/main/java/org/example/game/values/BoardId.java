package org.example.game.values;

import co.com.sofka.domain.generic.Identity;

public class BoardId extends Identity {

  public BoardId(String id) {
    super(id);
  }

  public BoardId() {
  }

  public static BoardId of(String id) {
    return new BoardId(id);
  }
}


