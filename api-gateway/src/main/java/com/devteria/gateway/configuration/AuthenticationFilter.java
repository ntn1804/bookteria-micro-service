package com.devteria.gateway.configuration;

import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

  private final IdentityService identityService;

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
    return identityService.introspectToken(token).flatMap(introspectResponseApiResponse -> {
      if (introspectResponseApiResponse.getResult().isValid()) {
        return chain.filter(exchange);
      } else {
        return unauthenticated((exchange.getResponse()));
      }
    }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
  }

  @Override
  public int getOrder() {
    return -1;
  }

  Mono<Void> unauthenticated(ServerHttpResponse response) {
    ApiResponse<?> apiResponse = ApiResponse.builder().code(401).message("Unauthenticated").build();

    ObjectMapper objectMapper = new ObjectMapper();
    String body = null;
    try {
      body = objectMapper.writeValueAsString(apiResponse);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
  }
}
