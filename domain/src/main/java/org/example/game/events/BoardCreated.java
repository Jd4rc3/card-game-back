package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Set;
import org.example.game.values.BoardId;
import org.example.game.values.PlayerId;

public class BoardCreated extends DomainEvent {

  private final BoardId boardId;

  private final Set<PlayerId> playerIds;

  public BoardCreated(BoardId boardId, Set<PlayerId> playerIds) {
    super("game.boardCreated");
    this.boardId = boardId;
    this.playerIds = playerIds;
  }

  public BoardId getBoardId() {
    return boardId;
  }

  public Set<PlayerId> getPlayerIds() {
    return playerIds;
  }
}
