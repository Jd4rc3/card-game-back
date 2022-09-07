package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import java.util.Set;
import org.example.game.values.Card;
import org.example.game.values.PlayerId;

public class CardsAssignedToPlayer extends DomainEvent {

  private final Set<Card> betCards;
  private final PlayerId winnerId;

  private final Integer points;

  public CardsAssignedToPlayer(PlayerId winnerId, Set<Card> betCards, Integer points) {
    super("game.CardsAssignedToPlayer");
    this.betCards = betCards;
    this.winnerId = winnerId;
    this.points = points;
  }

  public Set<Card> getBetCards() {
    return betCards;
  }

  public Integer getPoints() {
    return points;
  }

  public PlayerId getWinnerId() {
    return winnerId;
  }
}
