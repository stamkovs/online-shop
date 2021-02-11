package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.config.AuthConfiguration;
import com.stamkovs.online.shop.rest.auth.security.CustomUserDetailsService;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import com.stamkovs.online.shop.rest.auth.security.UserPrincipal;
import com.stamkovs.online.shop.rest.auth.util.CookieUtils;
import com.stamkovs.online.shop.rest.converter.UserConverter;
import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.model.UserLoginDto;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

/**
 * Service for logging in the user.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

  private final TokenProvider tokenProvider;
  private final CustomUserDetailsService customUserDetailsService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserConverter userConverter;
  private final AuthConfiguration authConfiguration;

  public void loginUser(HttpServletRequest request, HttpServletResponse response, UserLoginDto userLoginDto) throws UserNotFoundException {

    Optional<UserAccount> optionalUserAccount = userRepository.findByEmailIgnoreCase(userLoginDto.getEmail());
    if (optionalUserAccount.isPresent() && passwordEncoder.matches(userLoginDto.getPassword(),
      optionalUserAccount.get().getPassword())) {
      UserAccount userAccount = optionalUserAccount.get();
      UserDetails userDetails = customUserDetailsService.loadUserById(request, response, userAccount.getId());
      UserPrincipal userPrincipal = userConverter.convertToUserPrincipal(userDetails, userAccount);

      passwordEncoder.matches(userLoginDto.getPassword(), userAccount.getPassword());
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
        userPrincipal.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);
      int tokenExpirationInSeconds = authConfiguration.getOAuth().getTokenExpirationMsec().intValue() / 1000;
      CookieUtils.addAuthorizationCookies(response, tokenProvider.createToken(authentication),
        tokenExpirationInSeconds);
    } else {
      throw new UserNotFoundException("Invalid email or password.");
    }
  }
}
