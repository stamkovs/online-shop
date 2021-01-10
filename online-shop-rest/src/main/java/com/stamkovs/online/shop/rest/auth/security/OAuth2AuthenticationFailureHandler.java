package com.stamkovs.online.shop.rest.auth.security;

import com.stamkovs.online.shop.rest.auth.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.stamkovs.online.shop.rest.auth.security.HttpCookieOAuth2AuthorizationRequestRepository
  .REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.stamkovs.online.shop.rest.model.ShopConstants.FORWARD_SLASH;

/**
 * Handler for OAuth errors during authentication.
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException {
    String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
      .map(Cookie::getValue)
      .orElse((FORWARD_SLASH));

    targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
      .queryParam("error", exception.getLocalizedMessage())
      .build().toUriString();

    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
