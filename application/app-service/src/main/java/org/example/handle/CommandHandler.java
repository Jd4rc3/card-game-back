package org.example.handle;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import lombok.extern.slf4j.Slf4j;
import org.example.game.commands.CreateGameCommand;
import org.example.usecase.CreateGameUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class CommandHandler {

  private final IntegrationHandle integrationHandle;

  private final ErrorHandler errorHandler;

  public CommandHandler(IntegrationHandle integrationHandle, ErrorHandler errorHandler) {
    this.integrationHandle = integrationHandle;
    this.errorHandler = errorHandler;
  }

  @Bean
  public RouterFunction<ServerResponse> crear(CreateGameUseCase useCase) {
    return route(
        POST("/game/create").and(accept(MediaType.APPLICATION_JSON)),

        request -> useCase.andThen(integrationHandle)
            .apply(request.bodyToMono(CreateGameCommand.class))
            .then(ServerResponse.ok().build())
            .onErrorResume(errorHandler::error)
    );
  }
}