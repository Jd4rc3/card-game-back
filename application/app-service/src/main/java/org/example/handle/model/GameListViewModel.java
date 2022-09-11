package org.example.handle.model;

import java.util.Map;
import lombok.Data;

@Data
public class GameListViewModel {

  private String id;

  private Boolean started;

  private Boolean finished;

  private String uid;

  private Integer numberOfPlayers;

  private Map<String, String> players;

  private Player winner;

  @Data
  public static class Player {

    private String alias;

    private String playerId;
  }
}