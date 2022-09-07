package org.example.game.values;

import co.com.sofka.domain.generic.ValueObject;
import java.util.Objects;

public class Card implements ValueObject<Card.Props>, Comparable<Card> {

  private final MasterCardId cardId;

  private final Boolean isHidden;

  private final Boolean isEnable;

  private final Integer power;

  public Card(MasterCardId cardId, Boolean isHidden, Boolean isEnable, Integer power) {
    this.cardId = cardId;
    this.isHidden = isHidden;
    this.isEnable = isEnable;
    this.power = power;
  }

  @Override
  public Props value() {
    return new Props() {
      @Override
      public MasterCardId cardId() {
        return cardId;
      }

      @Override
      public Integer power() {
        return power;
      }

      @Override
      public Boolean isHidden() {
        return isHidden;
      }

      @Override
      public Boolean isEnable() {
        return isEnable;
      }
    };
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Card card)) {
      return false;
    }
    return Objects.equals(cardId, card.cardId) && Objects.equals(isHidden,
        card.isHidden) && Objects.equals(isEnable, card.isEnable)
        && Objects.equals(power, card.power);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cardId, isHidden, isEnable, power);
  }

  @Override
  public int compareTo(Card card) {
    return this.power - card.power;
  }

  public interface Props {

    MasterCardId cardId();

    Integer power();

    Boolean isHidden();

    Boolean isEnable();
  }
}
