
package com.swampgame.auth_service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swampgame.auth_service.dto.JwtPayload;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private final SecretKey key;
  private final ObjectMapper mapper = new ObjectMapper();

  public JwtService(@Value("${jwt.secret}") String secret) {
    System.err.println("USING SECRET KEY: " + secret);
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String issue(JwtPayload payload) {
    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> customClaims = mapper.convertValue(payload, Map.class);

      Map<String, Object> allClaims = new HashMap<>(customClaims);

      long now = System.currentTimeMillis();
      allClaims.put(Claims.ISSUED_AT, new Date(now));
      allClaims.put(Claims.EXPIRATION, new Date(now + 60 * 60 * 1000)); // 1hr ; in milliseconds

      return Jwts.builder()
          .claims(allClaims)
          .signWith(key, Jwts.SIG.HS256)
          .compact();
    } catch (Exception e) {
      throw new RuntimeException("Failed to issue JWT", e);
    }
  }

  public JwtPayload parse(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return mapper.convertValue(claims, JwtPayload.class);
  }

  public boolean validate(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new RuntimeException("JWT expired", e);
    } catch (JwtException e) {
      throw new RuntimeException("Invalid JWT", e);
    } catch (Exception e) {
      throw new RuntimeException("Failed to parse JWT", e);
    }
  }

}