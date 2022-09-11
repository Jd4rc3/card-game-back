package org.example.handle;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.example.handle.model.DeckViewModel;
import org.example.handle.model.GameListViewModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

@Configuration
public class QueryHandle {

  private final ReactiveMongoTemplate template;

  private final ErrorHandler errorHandler;

  public QueryHandle(ReactiveMongoTemplate template, ErrorHandler errorHandler) {
    this.template = template;
    this.errorHandler = errorHandler;
  }

  @Bean
  public RouterFunction<ServerResponse> listGame() {
    return RouterFunctions.route(
        GET("/games/"),

        request -> template.findAll(GameListViewModel.class,
                "gameview")
            .collectList()
            .flatMap(list -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    BodyInserters.fromPublisher(Flux.fromIterable(list), GameListViewModel.class)))
            .onErrorResume(errorHandler::error)
    );
  }

  @Bean
  public RouterFunction<ServerResponse> findDeck() {
    return RouterFunctions.route(
        GET("/decks/{uid}"),
        request -> template.find(filterByUId(request.pathVariable("uid")), DeckViewModel.class,
                "deckview")
            .collectList()
            .flatMap(list -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Flux.fromIterable(list), DeckViewModel.class))
                .onErrorResume(errorHandler::error))
    );
  }

  private Query filterByUId(String uid) {
    return new Query(Criteria.where("uid").is(uid));
  }
}
