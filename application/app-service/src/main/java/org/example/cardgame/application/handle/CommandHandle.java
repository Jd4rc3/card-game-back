package org.example.cardgame.application.handle;


import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.example.game.commands.CreateGameCommand;
import org.example.usecase.CreateGameUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CommandHandle {

  private final IntegrationHandle integrationHandle;

  public CommandHandle(IntegrationHandle integrationHandle) {
    this.integrationHandle = integrationHandle;
  }

  @Bean
  public RouterFunction<ServerResponse> crear(CreateGameUseCase usecase) {

    return route(
        POST("/juego/crear").and(accept(MediaType.APPLICATION_JSON)),
        request -> usecase.andThen(integrationHandle)
            .apply(request.bodyToMono(CreateGameCommand.class))
            .then(ServerResponse.ok().build())
    );
  }
}