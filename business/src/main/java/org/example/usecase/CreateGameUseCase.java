package org.example.usecase;

import co.com.sofka.business.generic.BusinessException;
import co.com.sofka.domain.generic.DomainEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.game.Game;
import org.example.game.PlayerFactory;
import org.example.game.commands.CreateGameCommand;
import org.example.game.values.Card;
import org.example.game.values.Deck;
import org.example.game.values.GameId;
import org.example.game.values.MasterCardId;
import org.example.game.values.PlayerId;
import org.example.gateway.CardsListService;
import org.example.gateway.model.MasterCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreateGameUseCase extends UseCaseForCommand<CreateGameCommand> {

  private final CardsListService cardsListService;

  private final Integer CARDS_PER_PLAYER = 5;

  private final Integer MIN_PLAYERS = 2;

  private final Integer MAX_PLAYERS = 6;

  public CreateGameUseCase(CardsListService cardsListService) {
    this.cardsListService = cardsListService;
  }

  @Override
  public Flux<DomainEvent> apply(Mono<CreateGameCommand> createGameCommandMono) {
    return cardsListService.getCardsFromMarvel().collectList()
        .flatMapMany(masterCards ->
            createGameCommandMono.flatMapIterable(createGameCommand ->
                {
                  if (createGameCommand.getPlayers().size() < MIN_PLAYERS
                      || createGameCommand.getPlayers().size() > MAX_PLAYERS) {
                    throw new BusinessException(createGameCommand.getGameId(),
                        "The number of players must be between " + MIN_PLAYERS + " and" + MAX_PLAYERS);
                  }

                  var cardsGame = buildCards(masterCards);
                  var factory = new PlayerFactory();

                  createGameCommand.getPlayers()
                      .forEach((id, alias) -> {

                        var deck = assignCardsToPlayer(cardsGame);

                        deck.forEach(card -> cardsGame.removeIf(
                            c -> c.value().cardId().equals(card.value().cardId())));

                        factory.addPlayer(PlayerId.of(id), alias, new Deck(deck));
                      });

                  var game = new Game(GameId.of(createGameCommand.getGameId()),
                      PlayerId.of(createGameCommand.getMainPlayerId()),
                      factory
                  );

                  return game.getUncommittedChanges();
                }
            ));
  }

  private List<Card> buildCards(List<MasterCard> masterCardList) {
    return masterCardList.stream()
        .map(masterCard -> new Card(MasterCardId.of(masterCard.getId()),
            true, true
        )).collect(Collectors.toList());
  }

  private Set<Card> assignCardsToPlayer(List<Card> cards) {
    var clonedCards = new ArrayList<>(cards);
    Collections.shuffle(clonedCards);

    return clonedCards.stream().limit(CARDS_PER_PLAYER).collect(Collectors.toSet());
  }
}
