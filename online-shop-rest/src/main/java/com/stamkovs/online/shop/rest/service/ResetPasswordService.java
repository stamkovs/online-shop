package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.model.ResetPasswordToken;
import com.stamkovs.online.shop.rest.auth.security.CustomUserDetailsService;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import com.stamkovs.online.shop.rest.auth.security.UserPrincipal;
import com.stamkovs.online.shop.rest.exception.UnauthorizedRedirectException;
import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import com.stamkovs.online.shop.rest.model.EmailDto;
import com.stamkovs.online.shop.rest.model.ResetPasswordDto;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.repository.ResetPasswordTokenRepository;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.stamkovs.online.shop.rest.model.ShopConstants.*;
import static com.stamkovs.online.shop.rest.model.ShopConstants.FORWARD_SLASH;

/**
 * Service for resetting user account password.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordService {

  private final EmailSenderService emailSenderService;
  private final UserRepository userRepository;
  private final ResetPasswordTokenRepository resetPasswordTokenRepository;
  private final CustomUserDetailsService customUserDetailsService;
  private final TokenProvider tokenProvider;
  private final PasswordEncoder passwordEncoder;

  public void resetPassword(EmailDto emailDto) {

    Optional<UserAccount> userAccount = userRepository.findByEmailIgnoreCase(emailDto.getEmail());

    if (userAccount.isEmpty() || Boolean.FALSE.equals(userAccount.get().getEmailVerified())) {
      log.info("User with email: {} does not exists or is not verified.", emailDto.getEmail());
      throw new UserNotFoundException("Account with email " + emailDto.getEmail() + " does not exist.");
    } else {
      log.info("User with email: {} request to reset password.", userAccount.get().getEmail());
      ResetPasswordToken resetPasswordToken;
      resetPasswordToken = createResetPasswordToken(userAccount.get());
      resetPasswordTokenRepository.save(resetPasswordToken);
      SimpleMailMessage mailMessage = emailSenderService.constructResetPasswordEmail(userAccount.get(),
        resetPasswordToken.getResetPasswordToken());
      emailSenderService.sendEmail(mailMessage);
    }
  }

  public ResetPasswordDto getUserDetailsByResetPasswordToken(String resetPasswordToken) {
    ResetPasswordToken token =
      resetPasswordTokenRepository.findByResetPasswordToken(resetPasswordToken);
    if (token == null || token.isUsed()) {
      throw new UnauthorizedRedirectException("Invalid url");
    }
    UserAccount userAccount = userRepository.findByAccountId(token.getUserAccountId());
    ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
    resetPasswordDto.setEmail(userAccount.getEmail());

    return resetPasswordDto;
  }

  public void updatePassword(HttpServletRequest request, HttpServletResponse response, String resetPasswordToken,
                             ResetPasswordDto resetPasswordDto) {
    ResetPasswordToken token =
      resetPasswordTokenRepository.findByResetPasswordToken(resetPasswordToken);
    if (token == null || token.isUsed()) {
      throw new UnauthorizedRedirectException("Invalid url");
    }
    UserAccount userAccount = userRepository.findByAccountId(token.getUserAccountId());
    if (userAccount != null) {
      userAccount.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
      UserDetails userDetails = customUserDetailsService.loadUserById(request, response, userAccount.getId());
      UserPrincipal userPrincipal = new UserPrincipal();
      userPrincipal.setAuthorities(userDetails.getAuthorities());
      userPrincipal.setEmail(userAccount.getEmail());
      userPrincipal.setId(userAccount.getId());
      userPrincipal.setPassword(userAccount.getPassword());
      resetPasswordTokenRepository.save(token);

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

      token.setUsed(true);
    }
  }

  private ResetPasswordToken createResetPasswordToken(UserAccount userAccount) {
    ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
    resetPasswordToken.setUserAccountId(userAccount.getAccountId());
    resetPasswordToken.setCreatedDate(new Date());
    resetPasswordToken.setResetPasswordToken(UUID.randomUUID().toString());

    return resetPasswordToken;
  }
}
