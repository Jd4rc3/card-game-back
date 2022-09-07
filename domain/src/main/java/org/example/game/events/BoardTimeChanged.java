package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.values.BoardId;

public class BoardTimeChanged extends
    DomainEvent {

  private final BoardId boardId;

  private final Integer time;

  public BoardTimeChanged(BoardId boardId, Integer time) {
    super("game.BoardTimeChanged");
    this.boardId = boardId;
    this.time = time;
  }

  public BoardId getBoardId() {
    return boardId;
  }

  public Integer getTime() {
    return time;
  }
}
