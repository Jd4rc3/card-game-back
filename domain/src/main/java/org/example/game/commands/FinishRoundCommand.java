package org.example.game.commands;

import co.com.sofka.domain.generic.Command;
import java.util.Set;
import org.example.game.entities.Player;
import org.example.game.values.BoardId;
import org.example.game.values.GameId;
import org.example.game.values.PlayerId;

public class FinishRoundCommand extends Command {

  private final GameId gameId;

  private final Set<PlayerId> players;

  private final BoardId boardId;

  public FinishRoundCommand(GameId gameId, BoardId boardId, Set<PlayerId> players) {
    this.gameId = gameId;
    this.boardId = boardId;
    this.players = players;
  }

  public GameId getGameId() {
    return gameId;
  }

  public BoardId getBoardId() {
    return boardId;
  }

  public Set<PlayerId> getPlayers() {
    return players;
  }
}