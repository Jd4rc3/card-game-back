package org.example.game.commands;

import co.com.sofka.domain.generic.Command;
import java.util.Map;
import org.example.game.entities.Player;

public class CreateGameCommand extends Command {

  private String gameId;

  private Map<String, String> players;

  private Player mainPlayer;

  public CreateGameCommand(String gameId, Map<String, String> players, Player mainPlayer) {
    this.gameId = gameId;
    this.players = players;
    this.mainPlayer = mainPlayer;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public Map<String, String> getPlayers() {
    return players;
  }

  public void setPlayers(Map<String, String> players) {
    this.players = players;
  }

  public Player getMainPlayer() {
    return mainPlayer;
  }

  public void setMainPlayer(Player mainPlayer) {
    this.mainPlayer = mainPlayer;
  }
}