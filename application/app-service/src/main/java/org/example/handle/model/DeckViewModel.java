package org.example.handle.model;

import java.util.Set;
import lombok.Data;


@Data
public class DeckViewModel {

  private String uid;

  private Integer numberOfCards;

  private Set<Card> deck;

  @Data
  public static class Card {

    private String cardId;

    private Boolean isHidden;

    private Boolean isEnable;

    private Integer power;

    private String uri;
  }
}
