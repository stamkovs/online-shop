package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.model.ConfirmationToken;
import com.stamkovs.online.shop.rest.auth.security.CustomUserDetailsService;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import com.stamkovs.online.shop.rest.auth.security.UserPrincipal;
import com.stamkovs.online.shop.rest.converter.UserConverter;
import com.stamkovs.online.shop.rest.exception.UnauthorizedRedirectException;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.model.UserRegisterDto;
import com.stamkovs.online.shop.rest.repository.ConfirmationTokenRepository;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.stamkovs.online.shop.rest.model.ShopConstants.*;
import static com.stamkovs.online.shop.rest.model.ShopConstants.FORWARD_SLASH;

/**
 * Service for registering user accounts.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterService {

  private final UserRepository userRepository;
  private final UserConverter userConverter;
  private final ConfirmationTokenRepository confirmationTokenRepository;
  private final EmailSenderService emailSenderService;
  private final CustomUserDetailsService customUserDetailsService;
  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;

  public void registerUser(UserRegisterDto userRegisterDto) {
    Optional<UserAccount> userAccount = userRepository.findByEmailIgnoreCase(userRegisterDto.getEmail());

    if (userAccount.isPresent() && Boolean.TRUE.equals(userAccount.get().getEmailVerified())) {
      log.info("User with email {} exists and is already verified", userAccount.get().getEmail());
    } else {
      ConfirmationToken confirmationToken;
      UserAccount account;
      if (userAccount.isEmpty()) {
        UserAccount newUser = userConverter.convertUserRegisterDtoToUserAccount(userRegisterDto);
        confirmationToken = createConfirmationToken(newUser);
        account = newUser;
        userRepository.save(newUser);
      } else {
        UserAccount existingUserAccount = userAccount.get();
        existingUserAccount.setFirstName(userRegisterDto.getFirstName());
        existingUserAccount.setLastName(userRegisterDto.getLastName());
        existingUserAccount.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        account = existingUserAccount;
        userRepository.save(existingUserAccount);
        confirmationToken = createConfirmationToken(existingUserAccount);
      }
      confirmationTokenRepository.save(confirmationToken);
      SimpleMailMessage mailMessage = emailSenderService.constructAccountVerificationEmail(account,
        confirmationToken.getConfirmationToken());
      emailSenderService.sendEmail(mailMessage);
    }
  }

  public UserRegisterDto getUserDetailsByConfirmationToken(HttpServletResponse response, String confirmationToken) {
    ConfirmationToken token =
      confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    if (token == null || token.isUsed()) {
      throw new UnauthorizedRedirectException("Invalid url");
    }
    UserRegisterDto userRegisterDto = new UserRegisterDto();
    UserAccount userAccount = userRepository.findByAccountId(token.getUserAccountId());
    if (userAccount != null) {
      userAccount.setEmailVerified(true);
      UserDetails userDetails = customUserDetailsService.loadUserById(userAccount.getId());
      UserPrincipal userPrincipal = new UserPrincipal();
      userPrincipal.setAuthorities(userDetails.getAuthorities());
      userPrincipal.setEmail(userAccount.getEmail());
      userPrincipal.setId(userAccount.getId());
      userPrincipal.setPassword(userAccount.getPassword());
      token.setUsed(true);
      confirmationTokenRepository.save(token);

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null,
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
    }
    return userRegisterDto;
  }

  private ConfirmationToken createConfirmationToken(UserAccount newUser) {
    ConfirmationToken confirmationToken = new ConfirmationToken();
    confirmationToken.setUserAccountId(newUser.getAccountId());
    confirmationToken.setCreatedDate(new Date());
    confirmationToken.setConfirmationToken(UUID.randomUUID().toString());

    return confirmationToken;
  }
}
