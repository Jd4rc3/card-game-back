package org.example.cardgame.application.adapters.service;


import org.example.gateway.CardsListService;
import org.example.gateway.model.MasterCard;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DataConsultarCartaMaestraService implements CardsListService {

  private final ReactiveMongoTemplate template;

  public DataConsultarCartaMaestraService(ReactiveMongoTemplate template) {
    this.template = template;
  }

  @Override
  public Flux<MasterCard> getCardsFromMarvel() {
    return template.findAll(MasterCard.class, "cards");
  }
}