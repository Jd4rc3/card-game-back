package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.values.BoardId;
import org.example.game.values.Card;
import org.example.game.values.PlayerId;

public class CardRemovedFromBoard extends DomainEvent {

  private final BoardId boardId;

  private final PlayerId playerId;

  private final Card card;

  public CardRemovedFromBoard(BoardId boardId, PlayerId playerId, Card card) {
    super("game.CardRemovedFromBoard");
    this.boardId = boardId;
    this.playerId = playerId;
    this.card = card;
  }

  public BoardId getBoardId() {
    return boardId;
  }

  public PlayerId getPlayerId() {
    return playerId;
  }

  public Card getCard() {
    return card;
  }
}
