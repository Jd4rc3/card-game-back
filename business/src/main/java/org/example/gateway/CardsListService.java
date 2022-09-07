package org.example.gateway;

import org.example.gateway.model.MasterCard;
import reactor.core.publisher.Flux;

public interface CardsListService {

  Flux<MasterCard> getCardsFromMarvel();
}
