package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.values.Card;
import org.example.game.values.PlayerId;

public class CardRemovedFromDeck extends DomainEvent {

  private final PlayerId playerId;
  private final Card card;

  public CardRemovedFromDeck(PlayerId playerId, Card card) {
    super("CardRemovedFromDeck");
    this.playerId = playerId;
    this.card = card;
  }

  public PlayerId getPlayerId() {
    return playerId;
  }

  public Card getCard() {
    return card;
  }

}
