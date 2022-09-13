package org.example.game.commands;

import co.com.sofka.domain.generic.Command;
import java.util.Set;
import org.example.game.values.BoardId;
import org.example.game.values.GameId;
import org.example.game.values.PlayerId;

public class FinishRoundCommand extends Command {

  private GameId gameId;

  private Set<PlayerId> players;

  private BoardId boardId;

  public GameId getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = GameId.of(gameId);
  }

  public Set<PlayerId> getPlayers() {
    return players;
  }

  public BoardId getBoardId() {
    return boardId;
  }
}