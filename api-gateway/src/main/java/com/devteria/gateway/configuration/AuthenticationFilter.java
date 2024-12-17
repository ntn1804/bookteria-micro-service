package com.devteria.gateway.configuration;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    log.info("hihi");
    // Get token from request header
    List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
    if (CollectionUtils.isEmpty(authHeader)) {
      return unauthenticated(exchange.getResponse());
    }

    String token = authHeader.getFirst().replace("Bearer ", "");
    log.info("Token: {}", token);

    // Verify token (delegate identity service to authenticate)


    // ...

    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return -1;
  }

  Mono<Void> unauthenticated(ServerHttpResponse response) {
    String body = "Unauthenticated";
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
  }
}
