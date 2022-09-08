package org.example.game.events;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.game.values.Deck;
import org.example.game.values.PlayerId;

public class PlayerAdded extends DomainEvent {

  private final PlayerId identity;

  private final String alias;

  private final Deck deck;

  public PlayerAdded(PlayerId identity, String alias, Deck deck) {
    super("game.PlayerAdded");

    this.identity = identity;
    this.alias = alias;
    this.deck = deck;
  }

  public PlayerId getIdentity() {
    return identity;
  }

  public String getAlias() {
    return alias;
  }

  public Deck getDeck() {
    return deck;
  }

  @Override
  public String toString() {
    return "PlayerAdded{" +
        "identity=" + identity +
        ", alias='" + alias + '\'' +
        ", deck=" + deck +
        '}';
  }
}
