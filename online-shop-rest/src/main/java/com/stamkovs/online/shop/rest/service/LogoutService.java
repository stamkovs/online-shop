package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.security.CustomUserDetailsService;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.stamkovs.online.shop.rest.auth.util.CookieUtils.getJwtFromRequest;
import static com.stamkovs.online.shop.rest.model.ShopConstants.*;
import static com.stamkovs.online.shop.rest.model.ShopConstants.FORWARD_SLASH;

/**
 * Service for logging out the user by clearing the cookies.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutService {

  private final TokenProvider tokenProvider;
  private final CustomUserDetailsService customUserDetailsService;

  public void logoutUser(HttpServletRequest request, HttpServletResponse response) {
    String jwt = getJwtFromRequest(request);
    UserDetails userDetails = null;
    Long userId = null;
    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
      userId = tokenProvider.getUserIdFromToken(jwt);

      userDetails = customUserDetailsService.loadUserById(userId);
    }
    Cookie revokeAuthorizationToken = new Cookie(AUTHORIZATION, EMPTY_STRING);
    revokeAuthorizationToken.setMaxAge(0);
    revokeAuthorizationToken.setPath(FORWARD_SLASH);
    revokeAuthorizationToken.setHttpOnly(true);

    Cookie logoutUser = new Cookie(IS_USER_LOGGED_IN, EMPTY_STRING);
    logoutUser.setPath(FORWARD_SLASH);
    logoutUser.setMaxAge(0);
    response.addCookie(revokeAuthorizationToken);
    response.addCookie(logoutUser);
    if (userDetails != null) {

      log.info("Successfully logged out user {}.", userId);
    }
    SecurityContextHolder.clearContext();
  }
}
