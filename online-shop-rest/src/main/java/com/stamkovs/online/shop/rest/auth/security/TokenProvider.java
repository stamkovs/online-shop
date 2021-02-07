package com.stamkovs.online.shop.rest.auth.security;

import com.stamkovs.online.shop.rest.auth.config.AuthConfiguration;
import com.stamkovs.online.shop.rest.auth.util.CookieUtils;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.stamkovs.online.shop.rest.model.ShopConstants.AUTHORIZATION;

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

  public boolean validateToken(HttpServletRequest request, HttpServletResponse response, String authToken) throws JwtException {
    try {
      Jwts.parser().setSigningKey(authConfiguration.getOAuth().getTokenSecret()).parseClaimsJws(authToken);
    } catch (ExpiredJwtException e) {
      log.info("Revoking expired JWT token.");
      CookieUtils.deleteCookie(request, response, AUTHORIZATION);
      return false;
    }
    return true;

    // ToDO: re-check if catching of exceptions will be needed.
  }
}
