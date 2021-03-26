package com.stamkovs.online.shop.rest.auth.security;

import com.stamkovs.online.shop.rest.auth.config.AuthConfiguration;
import com.stamkovs.online.shop.rest.auth.util.CookieUtils;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.model.UserRole;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.stamkovs.online.shop.rest.model.ShopConstants.*;

/**
 * Service for the creating and validating the token.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

  private final AuthConfiguration authConfiguration;
  private final UserRepository userRepository;

  public String createToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + authConfiguration.getOAuth().getTokenExpirationMsec());

    Map<String, Object> customHeader = new HashMap<>();
    Optional<UserAccount> optionalUserAccount = userRepository.findById(userPrincipal.getId());
    if (optionalUserAccount.isPresent()) {
      UserAccount userAccount = optionalUserAccount.get();
      customHeader.put("admin", "" + UserRole.ADMIN.equals(UserRole.getByCode(userAccount.getUserRoleId())));
      customHeader.put("email", userPrincipal.getEmail());
    }
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
      CookieUtils.deleteCookie(request, response, IS_USER_LOGGED_IN);
      return false;
    }
    return true;
  }
}
