package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.config.AuthConfiguration;
import com.stamkovs.online.shop.rest.auth.model.ConfirmationToken;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import com.stamkovs.online.shop.rest.auth.security.UserPrincipal;
import com.stamkovs.online.shop.rest.auth.util.CookieUtils;
import com.stamkovs.online.shop.rest.converter.UserConverter;
import com.stamkovs.online.shop.rest.exception.UnauthorizedRedirectException;
import com.stamkovs.online.shop.rest.exception.UserAlreadyExistsException;
import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.model.UserRegisterDto;
import com.stamkovs.online.shop.rest.repository.ConfirmationTokenRepository;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.stamkovs.online.shop.rest.model.ShopConstants.AUTHORIZATION;
import static com.stamkovs.online.shop.rest.model.ShopConstants.IS_USER_LOGGED_IN;

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
  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final AuthConfiguration authConfiguration;

  public void registerUser(UserRegisterDto userRegisterDto) {
    Optional<UserAccount> userAccount = userRepository.findByEmailIgnoreCase(userRegisterDto.getEmail());

    if (userAccount.isPresent() && Boolean.TRUE.equals(userAccount.get().getEmailVerified())) {
      throw new UserAlreadyExistsException("User with email " + userAccount.get().getEmail() + " exists and is " +
        "verified.");
    } else {
      ConfirmationToken confirmationToken;
      UserAccount account;
      log.info("Started registration for user with email: {}.", userRegisterDto.getEmail());
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

  public UserRegisterDto getUserDetailsByConfirmationToken(HttpServletRequest request, HttpServletResponse response,
                                                           String confirmationToken) {
    ConfirmationToken token =
      confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    if (token == null || token.isUsed()) {
      throw new UnauthorizedRedirectException("Invalid url");
    }
    UserRegisterDto userRegisterDto = new UserRegisterDto();
    UserAccount userAccount = userRepository.findByAccountId(token.getUserAccountId());
    if (userAccount == null) {
      // delete cookies in case user was deleted and tries to register again but jwt is still in his browser.
      CookieUtils.deleteCookie(request, response, AUTHORIZATION);
      CookieUtils.deleteCookie(request, response, IS_USER_LOGGED_IN);
      log.info("Revoking authorization bearer token cookie as user does not exists within the system.");
      throw new UserNotFoundException("User with id " + token.getUserAccountId() + " cant be found.");
    }

    userRegisterDto.setEmail(userAccount.getEmail());
    userAccount.setEmailVerified(true);
    UserPrincipal userPrincipal = userConverter.convertToUserPrincipal(userAccount);

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal,
      null,
      userPrincipal.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);
    int tokenExpirationInSeconds = authConfiguration.getOAuth().getTokenExpirationMsec().intValue() / 1000;

    CookieUtils.addAuthorizationCookies(response, tokenProvider.createToken(authentication),
      tokenExpirationInSeconds);

    token.setUsed(true);
    confirmationTokenRepository.save(token);
    userRepository.save(userAccount);

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
