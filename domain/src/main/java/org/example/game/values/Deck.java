package org.example.game.values;

import co.com.sofka.domain.generic.ValueObject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Deck implements ValueObject<Deck.Props> {

  private final Set<Card> cards;

  private final Integer quantity;

  public Deck(Set<Card> cards) {
    this.cards = cards;
    this.quantity = cards.size();
  }

  public Deck newCard(Card card) {
    var newCards = new HashSet<>(this.cards);
    newCards.add(card);
    return new Deck(newCards);
  }

  public Deck removeCard(Card card) {
    var cardId = card.value().cardId();
    var newDeck = this.cards.stream()
        .filter(carta -> !cardId.equals(carta.value().cardId()))
        .collect(Collectors.toCollection(HashSet::new));
    return new Deck(newDeck);
  }

  @Override
  public Props value() {
    return new Props() {
      @Override
      public Integer quantity() {
        return quantity;
      }

      @Override
      public Set<Card> cards() {
        return cards;
      }
    };
  }

  public interface Props {

    Integer quantity();

    Set<Card> cards();
  }
}
