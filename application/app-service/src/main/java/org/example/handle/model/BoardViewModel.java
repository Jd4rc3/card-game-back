package org.example.handle.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import org.example.handle.model.DeckViewModel.Card;

@Data
public class BoardViewModel {

  private Board board;

  private Integer time;

  private Round round;

  @Data
  public static class Board {

    private String id;

    private Set<String> players;

    private Boolean isEnable;

    private Map<String, List<Card>> cards;
  }

  @Data
  public static class Round {

    private Set<String> players;

    private String number;

    private Boolean isStarted;
  }
}