package org.example.game.commands;

import co.com.sofka.domain.generic.Command;
import java.util.Map;

public class CreateGameCommand extends Command {

  private String gameId;

  private Map<String, String> players;

  private String mainPlayerId;

  public CreateGameCommand(String gameId, Map<String, String> players, String mainPlayerId) {
    this.gameId = gameId;
    this.players = players;
    this.mainPlayerId = mainPlayerId;
  }

  public CreateGameCommand() {
  }

  public String getGameId() {
    return gameId;
  }

  public Map<String, String> getPlayers() {
    return players;
  }

  public void setPlayers(Map<String, String> players) {
    this.players = players;
  }

  public String getMainPlayerId() {
    return mainPlayerId;
  }
}