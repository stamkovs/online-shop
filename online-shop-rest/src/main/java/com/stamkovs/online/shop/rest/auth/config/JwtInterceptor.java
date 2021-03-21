package com.stamkovs.online.shop.rest.auth.config;

import com.stamkovs.online.shop.rest.auth.security.CustomUserDetailsService;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import com.stamkovs.online.shop.rest.auth.util.CookieUtils;
import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.stamkovs.online.shop.rest.auth.util.CookieUtils.getJwtFromRequest;
import static com.stamkovs.online.shop.rest.model.ShopConstants.*;

/**
 * Interceptor for the checking and validating the jwt if user is logged in or not, or authorized
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtInterceptor extends HandlerInterceptorAdapter {

  private final TokenProvider tokenProvider;

  private final CustomUserDetailsService customUserDetailsService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    log.info("Checking if user is authenticated and authorized..");

    Cookie isUserLoggedIn = new Cookie(IS_USER_LOGGED_IN, "1");
    isUserLoggedIn.setPath(FORWARD_SLASH);

    String jwt = getJwtFromRequest(request);
    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(request, response, jwt)) {
      Long userId = tokenProvider.getUserIdFromToken(jwt);
      try {
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        String userAccountId = customUserDetailsService.loadUserAccountById(userId).getAccountId();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
          userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        isUserLoggedIn.setMaxAge(86000);
        response.addCookie(isUserLoggedIn);
        log.info("User {} is logged in and authorized.", userAccountId);
      } catch (UserNotFoundException e) {
        CookieUtils.deleteCookie(request, response, AUTHORIZATION);
        CookieUtils.deleteCookie(request, response, IS_USER_LOGGED_IN);
        CookieUtils.deleteCookie(request, response, "is_user_admin");
        CookieUtils.deleteCookie(request, response, "user_auth_information");
        log.info("Revoking authorization bearer token cookie as user does not exists within the system.");
        throw new UserNotFoundException("User with id " + userId + " cant be found.");
      }
    } else {
      isUserLoggedIn.setValue(EMPTY_STRING);
      isUserLoggedIn.setMaxAge(0);
      response.addCookie(isUserLoggedIn);
      log.info("User is anonymous, but can view public pages.");
    }
    return true;
//    throw new JwtException("User not authorized or not logged in.");
  }

}
