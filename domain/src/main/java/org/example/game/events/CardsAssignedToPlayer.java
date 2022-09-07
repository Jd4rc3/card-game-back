package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Set;
import org.example.game.values.Card;
import org.example.game.values.PlayerId;

public class CardsAssignedToPlayer extends DomainEvent {

  private final Set<Card> cards;
  private final PlayerId playerId;

  public CardsAssignedToPlayer(PlayerId playerId, Set<Card> cards) {
    super("game.CardsAssignedToPlayer");
    this.cards = cards;
    this.playerId = playerId;
  }

  public Set<Card> getCards() {
    return cards;
  }

  public PlayerId getPlayerId() {
    return playerId;
  }
}
