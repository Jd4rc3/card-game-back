package org.example.game;

import co.com.sofka.domain.generic.EventChange;
import java.util.HashMap;
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

public class GameEventChange extends EventChange {

  public GameEventChange(Game game) {
    apply((GameCreated event) -> {
      game.Players = new HashMap<>();
      game.mainPlayer = event.getMainPlayer();
    });

    apply((PlayerAdded event) -> {
      game.Players.put(event.getIdentity(),
          new Player(event.getIdentity(), event.getAlias(), event.getDeck()));
    });

    apply((RoundCreated event) ->
    {
      game.round = event.getRound();
      game.board.adjustTime(event.getTime());
    });

    apply((BoardCreated event) -> {
      game.board = new Board(event.getBoardId(), event.getPlayerIds());
    });

    apply((BoardTimeChanged event) -> {
      game.board.adjustTime(event.getTime());
    });

    apply((CardPutOnBoard event) -> {
      if (Boolean.FALSE.equals
          (game.board.isEnable())) {
        throw new IllegalArgumentException("The board is disabled so you can't bet");
      }

      game.board.addMatch(event.getPlayerId(), event.getCard());
    });

    apply((CardRemovedFromBoard event) -> {
      game.board.removeCard(event.getPlayerId(), event.getCard());
    });

    apply((CardRemovedFromDeck event) -> {
      game.Players.get(event.getPlayerId()).removeCardFromDeck(event.getCard());
    });

    apply((RoundStarted event) -> {
      game.round = game.round.startRound();
      game.board.enableBet();
    });

    apply((RoundFinished event) -> {
      game.round = game.round.endRound();
      game.board.disableBet();
    });

    apply((CardsAssignedToPlayer event) -> {
      var player = game.players().get(event.getWinnerId());
      event.getBetCards().forEach(player::addCardToDeck);
    });

    apply((GameFinished event) -> {
      game.winner = game.players().get(event.getWinner());
    });
  }
}