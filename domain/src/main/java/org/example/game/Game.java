package org.example.game;

import co.com.sofka.domain.generic.AggregateEvent;
import co.com.sofka.domain.generic.DomainEvent;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.example.game.entities.Board;
import org.example.game.entities.Player;
import org.example.game.events.BoardCreated;
import org.example.game.events.BoardTimeChanged;
import org.example.game.events.CardPutOnBoard;
import org.example.game.events.CardRemovedFromBoard;
import org.example.game.events.CardRemovedFromDeck;
import org.example.game.events.CardsAssignedToPlayer;
import org.example.game.events.GameCreated;
import org.example.game.events.GameFinished;
import org.example.game.events.PlayerAdded;
import org.example.game.events.RoundCreated;
import org.example.game.events.RoundFinished;
import org.example.game.events.RoundStarted;
import org.example.game.values.BoardId;
import org.example.game.values.Card;
import org.example.game.values.GameId;
import org.example.game.values.PlayerId;
import org.example.game.values.Round;

public class Game extends AggregateEvent<GameId> {

  protected Map<PlayerId, Player> Players;

  protected Board board;

  protected Player winner;

  protected Round round;

  protected PlayerId mainPlayer;

  public Game(GameId gameId, PlayerId playerId, PlayerFactory playerFactory) {
    super(gameId);

    appendChange(new GameCreated(playerId)).apply();

    playerFactory.getPlayers()
        .forEach(player ->
            appendChange(new PlayerAdded(player.identity(), player.alias(), player.deck()))
        );
    subscribe(new GameEventChange(this));
  }

  public Game(GameId gameId) {
    super(gameId);
    subscribe(new GameEventChange(this));
  }

  public static Game from(GameId gameId, List<DomainEvent> events) {
    var game = new Game(gameId);
    events.forEach(game::applyEvent);
    return game;
  }

  public void createBoard() {
    var id = new BoardId();
    appendChange(new BoardCreated(id, Players.keySet())).apply();
  }

  public void createRound(Round round, Integer time) {
    appendChange(new RoundCreated(round, time)).apply();
  }

  public void chageBoardTime(BoardId boardId, Integer time) {
    appendChange(new BoardTimeChanged(boardId, time)).apply();
  }

  public void putCardOnBoard(BoardId boardId, PlayerId playerId, Card card) {
    appendChange(new CardPutOnBoard(boardId, playerId, card)).apply();
    appendChange(new CardRemovedFromDeck(playerId, card)).apply();
  }

  public void removeCardFromBoard(BoardId boardId, PlayerId playerId, Card card) {
    appendChange(new CardRemovedFromBoard(boardId, playerId, card)).apply();
  }

  public void startRound(Round round) {
    appendChange(new RoundStarted()).apply();
  }

  public void asignCardsToPlayer(PlayerId playerId, Set<Card> cards) {
    appendChange(new CardsAssignedToPlayer(playerId, cards)).apply();
  }


  public void finishRound(BoardId boardId, Set<PlayerId> playerIds) {
    appendChange(new RoundFinished(boardId, playerIds)).apply();
  }

  public void finishGame(PlayerId winnerId, String alias) {
    appendChange(new GameFinished(winnerId, alias)).apply();
  }

  public Round round() {
    return round;
  }

  public Board board() {
    return board;
  }

  public Map<PlayerId, Player> players() {
    return Players;
  }
}