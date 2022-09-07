package org.example.game.entities;

import co.com.sofka.domain.generic.Entity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.example.game.values.BoardId;
import org.example.game.values.Card;
import org.example.game.values.PlayerId;

public class Board extends Entity<BoardId> {

  private Integer timeInSeconds;

  private Boolean isEnable;

  private final Map<PlayerId, Set<Card>> match;

  public Board(BoardId entityId, Set<PlayerId> playerIds) {
    super(entityId);
    this.match = new HashMap<>();
    this.isEnable = false;
    playerIds.forEach(playerId -> match.put(playerId, new HashSet<>()));
  }

  public void adjustTime(Integer time) {
    this.timeInSeconds = time;
  }

  public Integer time() {
    return timeInSeconds;
  }

  public void addMatch(PlayerId playerId, Card card) {
    match.getOrDefault(playerId, new HashSet<>()).add(card);
  }

  public void removeCard(PlayerId playerId, Card card) {
    match.getOrDefault(playerId, new HashSet<>()).remove(card);
  }

  public void enableBet() {
    this.isEnable = true;
  }

  public void disableBet() {
    this.isEnable = false;
  }

  public void restartMatch() {
    match.clear();
  }

  public Boolean isEnable() {
    return isEnable;
  }

  public Map<PlayerId, Set<Card>> match() {
    return match;
  }
}