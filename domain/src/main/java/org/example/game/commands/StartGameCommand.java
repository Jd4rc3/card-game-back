package org.example.game.commands;

import co.com.sofka.domain.generic.Command;

public class StartGameCommand extends Command {

  private String gameId;

  public StartGameCommand() {
  }

  public StartGameCommand(String gameId) {
    this.gameId = gameId;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }
}