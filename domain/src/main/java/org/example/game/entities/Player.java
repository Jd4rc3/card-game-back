package org.example.game.entities;

import co.com.sofka.domain.generic.Entity;
import java.util.Objects;
import org.example.game.values.Card;
import org.example.game.values.Deck;
import org.example.game.values.PlayerId;

public class Player extends Entity<PlayerId> {

  private final String alias;

  private final Deck deck;

  public Player(PlayerId entityId, String alias, Deck deck) {
    super(entityId);
    this.alias = Objects.requireNonNull(alias);
    this.deck = Objects.requireNonNull(deck);

    if (deck.value().quantity() <= 0) {
      throw new IllegalArgumentException("The deck must contain cards");
    }
  }

  public void addCardToDeck(Card card) {
    deck.newCard(card);
  }

  public void removeCardFromDeck(Card card) {
    deck.removeCard(card);
  }

  public String alias() {
    return alias;
  }

  public Deck deck() {
    return deck;
  }
}
