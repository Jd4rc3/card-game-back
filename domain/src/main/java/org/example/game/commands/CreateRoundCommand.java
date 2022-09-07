package org.example.game.commands;

import co.com.sofka.domain.generic.Command;
import org.example.game.values.BoardId;
import org.example.game.values.GameId;

public class CreateRoundCommand extends Command {

  private GameId gameId;

  private BoardId boardId;

  private Integer time;

  public CreateRoundCommand(GameId gameId, BoardId boardId, Integer time) {
    this.gameId = gameId;
    this.boardId = boardId;
    this.time = time;
  }

  public GameId getGameId() {
    return gameId;
  }

  public void setGameId(GameId gameId) {
    this.gameId = gameId;
  }

  public BoardId getBoardId() {
    return boardId;
  }

  public void setBoardId(BoardId boardId) {
    this.boardId = boardId;
  }

  public Integer getTime() {
    return time;
  }

  public void setTime(Integer time) {
    this.time = time;
  }
}