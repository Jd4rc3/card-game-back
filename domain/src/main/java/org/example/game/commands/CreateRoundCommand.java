package org.example.game.commands;

import co.com.sofka.domain.generic.Command;
import java.util.Set;
import org.example.game.values.GameId;

public class CreateRoundCommand extends Command {

  private GameId gameId;

  private Integer time;

  private Set<String> players;

  public CreateRoundCommand(GameId gameId, Integer time, Set<String> players) {
    this.gameId = gameId;
    this.time = time;
    this.players = players;
  }

  public GameId getGameId() {
    return gameId;
  }

  public void setGameId(GameId gameId) {
    this.gameId = gameId;
  }

  public Integer getTime() {
    return time;
  }

  public void setTime(Integer time) {
    this.time = time;
  }

  public Set<String> getPlayers() {
    return players;
  }

  public void setPlayers(Set<String> players) {
    this.players = players;
  }
}