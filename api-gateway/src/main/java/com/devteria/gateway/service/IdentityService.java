package com.devteria.gateway.service;

import com.devteria.gateway.dto.request.IntrospectRequest;
import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.dto.response.IntrospectResponse;
import com.devteria.gateway.repository.IdentityClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IdentityService {

  private final IdentityClient identityClient;

  public Mono<ApiResponse<IntrospectResponse>> introspectToken(String token) {
    return identityClient.introspectToken(IntrospectRequest.builder().token(token).build());
  }
}
