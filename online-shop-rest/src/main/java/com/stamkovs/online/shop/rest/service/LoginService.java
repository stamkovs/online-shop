package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.security.CustomUserDetailsService;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import com.stamkovs.online.shop.rest.auth.security.UserPrincipal;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
  private final CustomUserDetailsService customUserDetailsService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void loginUser(HttpServletRequest request, HttpServletResponse response, UserLoginDto userLoginDto) throws UserNotFoundException {

    Optional<UserAccount> optionalUserAccount = userRepository.findByEmailIgnoreCase(userLoginDto.getEmail());
    if (optionalUserAccount.isPresent() && passwordEncoder.matches(userLoginDto.getPassword(),
      optionalUserAccount.get().getPassword())) {
      UserAccount userAccount = optionalUserAccount.get();
      UserDetails userDetails = customUserDetailsService.loadUserById(request, response, userAccount.getId());
      UserPrincipal userPrincipal = new UserPrincipal();
      userPrincipal.setAuthorities(userDetails.getAuthorities());
      passwordEncoder.matches(userLoginDto.getPassword(), userAccount.getPassword());
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);
      Cookie bearerCookie = new Cookie(AUTHORIZATION, tokenProvider.createToken(authentication));
      bearerCookie.setPath(FORWARD_SLASH);
      bearerCookie.setMaxAge(86000);
      bearerCookie.setHttpOnly(true);
      response.addCookie(bearerCookie);
      Cookie isUserLoggedIn = new Cookie(IS_USER_LOGGED_IN, "1");
      isUserLoggedIn.setPath(FORWARD_SLASH);
      isUserLoggedIn.setMaxAge(86000);
      response.addCookie(isUserLoggedIn);
    } else {
      throw new UserNotFoundException("Invalid email or password.");
    }
  }
}
