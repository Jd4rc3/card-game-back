package org.example.game.values;

import co.com.sofka.domain.generic.ValueObject;
import java.util.Set;

public class Round implements ValueObject<Round.Props> {

  private final Set<PlayerId> players;

  private final Integer number;

  private final Boolean isStarted;

  public Round(Integer number, Set<PlayerId> players) {
    this.players = players;
    this.number = number;
    this.isStarted = false;

    if (number <= 0) {
      throw new IllegalArgumentException("The round number cannot be zero or negative");
    }

    if (players.size() <= 1) {
      throw new IllegalArgumentException("The round must be created with a minimum of 2 players");
    }
  }

  public Round(Set<PlayerId> players, Integer number, Boolean isStarted) {
    this.players = players;
    this.number = number;
    this.isStarted = isStarted;
  }

  public Round startRound() {
    return new Round(this.players, this.number, true);
  }

  public Round endRound() {
    return new Round(this.players, this.number, false);
  }

  public Round incrementRound(Set<PlayerId> players) {
    return new Round(players, this.number + 1, false);
  }

  @Override
  public Props value() {
    return new Props() {
      @Override
      public Set<PlayerId> players() {
        return players;
      }

      @Override
      public Integer number() {
        return number;
      }

      @Override
      public Boolean isStarted() {
        return isStarted;
      }
    };
  }

  public interface Props {

    Set<PlayerId> players();

    Integer number();

    Boolean isStarted();
  }
}
