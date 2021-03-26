package com.stamkovs.online.shop.rest.auth.security;

import com.stamkovs.online.shop.rest.auth.config.AuthConfiguration;
import com.stamkovs.online.shop.rest.exception.UnauthorizedShoptasticException;
import com.stamkovs.online.shop.rest.auth.util.CookieUtils;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.model.UserRole;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.stamkovs.online.shop.rest.auth.security.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.stamkovs.online.shop.rest.model.ShopConstants.*;

/**
 * Success handler for the OAuth response.
 */
@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final TokenProvider tokenProvider;

  private final AuthConfiguration authConfiguration;

  private final UserRepository userRepository;

  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Autowired
  OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AuthConfiguration authConfiguration,
                                     HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                                     UserRepository userRepository) {
    this.tokenProvider = tokenProvider;
    this.authConfiguration = authConfiguration;
    this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    this.userRepository = userRepository;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException {
    String targetUrl = determineTargetUrl(request, response);

    if (response.isCommitted()) {
      log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }
    clearAuthenticationAttributes(request, response);
    String email = authentication.getName();
    Optional<UserAccount> optionalUserAccount = userRepository.findByEmailIgnoreCase(email);

    UserAccount userAccount = new UserAccount();
    if (optionalUserAccount.isPresent()) {
      userAccount = optionalUserAccount.get();
    }

    int tokenExpirationInSeconds = authConfiguration.getOAuth().getTokenExpirationMsec().intValue() / 1000;
    CookieUtils.addAuthorizationCookies(response, tokenProvider.createToken(authentication), tokenExpirationInSeconds
      , userAccount);

    if (UserRole.ADMIN.equals(UserRole.getByCode(userAccount.getUserRoleId()))) {
      Cookie adminUserCookie = new Cookie(IS_USER_ADMIN, TRUE);
      adminUserCookie.setPath(FORWARD_SLASH);
      adminUserCookie.setMaxAge(86000);
      response.addCookie(adminUserCookie);
    }

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
    Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
      .map(Cookie::getValue);

    if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
      throw new UnauthorizedShoptasticException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with "
        + "the authentication");
    }

    String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

    return UriComponentsBuilder.fromUriString(targetUrl)
      .build().toUriString();
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
  }

  private boolean isAuthorizedRedirectUri(String uri) {
    URI clientRedirectUri = URI.create(uri);

    return authConfiguration.getOAuth().getAuthorizedRedirectUris()
      .stream()
      .anyMatch(authorizedRedirectUri -> {
        // Only validate host and port. Let the clients use different paths if they want to
        URI authorizedURI = URI.create(authorizedRedirectUri);
        return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
          && authorizedURI.getPort() == clientRedirectUri.getPort();
      });
  }
}
