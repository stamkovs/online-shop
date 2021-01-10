package com.stamkovs.online.shop.rest.auth.security;

import com.stamkovs.online.shop.rest.auth.config.AuthConfiguration;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for the creating and validating the token.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

  private final AuthConfiguration authConfiguration;

  public String createToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + authConfiguration.getOAuth().getTokenExpirationMsec());

    Map<String, Object> customHeader = new HashMap<>();
    customHeader.put("admin", "false");
    customHeader.put("email", userPrincipal.getEmail());
    return Jwts.builder()
      .setClaims(customHeader)
      .setSubject(Long.toString(userPrincipal.getId()))
      .setExpiration(expiryDate)
      .signWith(SignatureAlgorithm.HS512, authConfiguration.getOAuth().getTokenSecret())
      .compact();
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = Jwts.parser()
      .setSigningKey(authConfiguration.getOAuth().getTokenSecret())
      .parseClaimsJws(token)
      .getBody();

    return Long.parseLong(claims.getSubject());
  }

  public boolean validateToken(String authToken) {
    Jwts.parser().setSigningKey(authConfiguration.getOAuth().getTokenSecret()).parseClaimsJws(authToken);
    return true;

    // ToDO: re-check if catching of exceptions will be needed.
  }
}
