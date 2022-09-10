package org.example.adapters.service;

import org.example.gateway.CardsListService;
import org.example.gateway.model.MasterCard;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class MasterCardsService implements CardsListService {

  private final ReactiveMongoTemplate template;


  public MasterCardsService(ReactiveMongoTemplate template) {
    this.template = template;
  }

  @Override
  public Flux<MasterCard> getCardsFromMarvel() {
    return null;
  }
}
