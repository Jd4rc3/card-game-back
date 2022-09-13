package org.example.handle.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import org.example.handle.model.DeckViewModel.Card;

@Data
public class BoardViewModel {

  private String boardId;

  private Round round;

  private Set<String> players;

  private String mainPlayerId;

  private Map<String, List<Card>> cards;

  @Data
  public static class Round {

    private Integer time;

    private Set<String> playersAlive;

    private Integer number;

    private Boolean isStarted;
  }
}