package org.example.handle.model;

import java.util.Set;
import lombok.Data;


public class DeckViewModel {

  private Integer quantity;

  private Set<Card> Cards;

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Set<Card> getCards() {
    return Cards;
  }

  public void setCards(Set<Card> cards) {
    Cards = cards;
  }

  @Data
  public static class Card {

    private String cardId;

    private String playerId;

    private Boolean isHidden;

    private Boolean isEnable;

    private Integer power;
  }
}
