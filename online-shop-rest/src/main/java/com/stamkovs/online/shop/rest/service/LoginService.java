package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.config.AuthConfiguration;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import com.stamkovs.online.shop.rest.auth.security.UserPrincipal;
import com.stamkovs.online.shop.rest.auth.util.CookieUtils;
import com.stamkovs.online.shop.rest.converter.UserConverter;
import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.model.UserLoginDto;
import com.stamkovs.online.shop.rest.model.UserRole;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static com.stamkovs.online.shop.rest.model.ShopConstants.*;

/**
 * Service for logging in the user.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

  private final TokenProvider tokenProvider;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserConverter userConverter;
  private final AuthConfiguration authConfiguration;

  public void loginUser(HttpServletResponse response, UserLoginDto userLoginDto) throws UserNotFoundException {

    Optional<UserAccount> optionalUserAccount = userRepository.findByEmailIgnoreCase(userLoginDto.getEmail());
    if (optionalUserAccount.isEmpty() || !passwordEncoder.matches(userLoginDto.getPassword(),
      optionalUserAccount.get().getPassword())) {
      throw new UserNotFoundException("Invalid email or password.");
    }
    UserAccount userAccount = optionalUserAccount.get();
    UserRole userRole = UserRole.getByCode(userAccount.getUserRoleId());
    UserPrincipal userPrincipal = userConverter.convertToUserPrincipal(userAccount, userRole);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal,
      null,
      userPrincipal.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);
    int tokenExpirationInSeconds = authConfiguration.getOAuth().getTokenExpirationMsec().intValue() / 1000;
    CookieUtils.addAuthorizationCookies(response, tokenProvider.createToken(authentication),
      tokenExpirationInSeconds, userAccount);
    if (UserRole.ADMIN.equals(userRole)) {
      Cookie adminUserCookie = new Cookie(IS_USER_ADMIN, TRUE);
      adminUserCookie.setPath(FORWARD_SLASH);
      adminUserCookie.setMaxAge(86000);
      response.addCookie(adminUserCookie);
    }

    log.info("User with id {} is successfully logged in.", userAccount.getAccountId());
  }
}
