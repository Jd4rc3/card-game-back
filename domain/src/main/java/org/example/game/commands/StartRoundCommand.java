package org.example.game.commands;

import co.com.sofka.domain.generic.Command;

public class StartRoundCommand extends Command {

  private String gameId;

  public String getGameId() {
    return gameId;
  }

  public StartRoundCommand(String gameId) {
    this.gameId = gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }
}
