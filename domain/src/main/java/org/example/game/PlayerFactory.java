package org.example.game;

import java.util.HashSet;
import java.util.Set;
import org.example.game.entities.Player;
import org.example.game.values.Deck;
import org.example.game.values.PlayerId;

public class PlayerFactory {

  private final Set<Player> players;

  public PlayerFactory() {
    players = new HashSet<>();
  }

  public void addPlayer(PlayerId playerId, String alias, Deck deck) {
    players.add(new Player(playerId, alias, deck));
  }

  protected Set<Player> getPlayers() {
    return players;
  }
}
