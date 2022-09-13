package org.example.handle;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.example.handle.model.BoardViewModel;
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
import reactor.core.publisher.Mono;

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
        GET("/games/{playerId}"),

        request -> template.find(findByUid(request.pathVariable("playerId")),
                GameListViewModel.class,
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
  public RouterFunction<ServerResponse> getBoard() {
    return RouterFunctions.route(
        GET("/{gameId}/board"),

        request -> template.findOne(
                filterByGameId(request.pathVariable("gameId")),
                BoardViewModel.class,
                "boardview")
            .flatMap(deck -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(deck), BoardViewModel.class)))
            .onErrorResume(errorHandler::error)
    );
  }

  @Bean
  public RouterFunction<ServerResponse> findDeck() {
    return RouterFunctions.route(
        GET("/deck/{playerId}/{gameId}"),
        request -> template.findOne(
                filterByUIdAndGameId(request.pathVariable("playerId"), request.pathVariable("gameId")),
                DeckViewModel.class,
                "deckview")
            .flatMap(list -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(list), DeckViewModel.class))
                .onErrorResume(errorHandler::error))
    );
  }

  private Query filterByUIdAndGameId(String uid, String gameId) {
    return new Query(Criteria.where("uid").is(uid).and("gameId").is(gameId));
  }

  private Query findByUid(String playerId) {
    Criteria criteria = new Criteria("players." + playerId).exists(true);

    return new Query(criteria);
  }

  private Query filterByGameId(String gameId) {
    return new Query(Criteria.where("_id").is(gameId));
  }
}
