package org.example.handle;

import java.util.function.BiFunction;
import org.example.handle.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ErrorHandler {

  private static final BiFunction<HttpStatus, String, Mono<ServerResponse>> response =
      (status, value) -> ServerResponse.status(status)
          .body(Mono.just(new ErrorResponse(value)), ErrorResponse.class);

  Mono<ServerResponse> error(Throwable error) {
    error.printStackTrace();
    return response.apply(HttpStatus.BAD_REQUEST, error.getMessage());
  }
}