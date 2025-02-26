package com.devteria.profile.configuration;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

  @Override
  public Jwt decode(String token) throws JwtException {

    try {
      SignedJWT signedJWT = SignedJWT.parse(token);

      JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

      Instant issuedAt = jwtClaimsSet.getIssueTime().toInstant();
      Instant expiresAt = jwtClaimsSet.getExpirationTime().toInstant();

      Map<String, Object> headers = signedJWT.getHeader().toJSONObject();
      Map<String, Object> claims = signedJWT.getJWTClaimsSet().getClaims();

      return new Jwt(token, issuedAt, expiresAt, headers, claims);
    } catch (ParseException e) {
      throw new JwtException("Invalid token");
    }
  }
}
