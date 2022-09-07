package org.example.game.commands;

import co.com.sofka.domain.generic.Command;

public class PutCardOnBoardCommand extends Command {

  private String playerId;
  private String cardId;
  private String gameId;

  public PutCardOnBoardCommand(String playerId, String cardId, String gameId) {
    this.playerId = playerId;
    this.cardId = cardId;
    this.gameId = gameId;
  }

  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public String getCardId() {
    return cardId;
  }

  public void setCardId(String cardId) {
    this.cardId = cardId;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }
}