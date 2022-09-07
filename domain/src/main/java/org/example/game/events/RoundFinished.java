package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Set;
import org.example.game.values.BoardId;
import org.example.game.values.PlayerId;

public class RoundFinished extends DomainEvent {

  private final Set<PlayerId> players;

  private final BoardId boardId;

  public RoundFinished(BoardId boardId, Set<PlayerId> playerIds) {
    super("game.RoundFinished");
    this.boardId = boardId;
    this.players = playerIds;
  }

  public Set<PlayerId> getPlayers() {
    return players;
  }

  public BoardId getBoardId() {
    return boardId;
  }
}
