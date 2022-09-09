package org.example.game.values;

import co.com.sofka.domain.generic.ValueObject;
import java.util.Objects;

public class Card implements ValueObject<Card.Props>, Comparable<Card> {

  private final MasterCardId cardId;

  private final Boolean isHidden;

  private final Boolean isEnable;

  private final Integer power;

  public Card(MasterCardId cardId, Boolean isHidden, Boolean isEnable) {
    this.cardId = cardId;
    this.isHidden = isHidden;
    this.isEnable = isEnable;
    this.power = 0;
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
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Card card = (Card) o;
    return Objects.equals(cardId, card.cardId) && Objects.equals(isHidden, card.isHidden)
        && Objects.equals(isEnable, card.isEnable) && Objects.equals(power, card.power);
  }

  @Override
  public int hashCode() {
    int result = cardId != null ? cardId.hashCode() : 0;
    result = 31 * result + (isHidden != null ? isHidden.hashCode() : 0);
    result = 31 * result + (isEnable != null ? isEnable.hashCode() : 0);
    result = 31 * result + (power != null ? power.hashCode() : 0);
    return result;
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
