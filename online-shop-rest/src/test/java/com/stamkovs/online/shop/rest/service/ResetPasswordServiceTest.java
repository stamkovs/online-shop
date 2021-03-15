package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.config.AuthConfiguration;
import com.stamkovs.online.shop.rest.auth.model.OAuth;
import com.stamkovs.online.shop.rest.auth.model.ResetPasswordToken;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import com.stamkovs.online.shop.rest.auth.security.UserPrincipal;
import com.stamkovs.online.shop.rest.converter.UserConverter;
import com.stamkovs.online.shop.rest.exception.UnauthorizedShoptasticException;
import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import com.stamkovs.online.shop.rest.model.EmailDto;
import com.stamkovs.online.shop.rest.model.ResetPasswordDto;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.repository.ResetPasswordTokenRepository;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link ResetPasswordService}.
 */
@ExtendWith(MockitoExtension.class)
class ResetPasswordServiceTest {

  private static final String TEST_EMAIL = "testEmail";
  @InjectMocks
  private ResetPasswordService resetPasswordService;

  @Mock
  private EmailSenderService emailSenderService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ResetPasswordTokenRepository resetPasswordTokenRepository;

  @Mock
  private TokenProvider tokenProvider;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserConverter userConverter;

  @Mock
  private AuthConfiguration authConfiguration;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Test
  void shouldCreateTokenAndSendEmailForPasswordReset() {
    // given
    UserAccount userAccount = new UserAccount();
    userAccount.setEmail(TEST_EMAIL);
    userAccount.setEmailVerified(true);
    EmailDto emailDto = new EmailDto();
    emailDto.setEmail(TEST_EMAIL);
    when(userRepository.findByEmailIgnoreCase(emailDto.getEmail())).thenReturn(Optional.of(userAccount));

    // when
    resetPasswordService.resetPassword(emailDto);

    // then
    verify(resetPasswordTokenRepository, times(1)).save(any());
    verify(emailSenderService, times(1)).sendEmail(any(), any());
  }

  @Test
  void shouldThrowExceptionIfAccountDoesNotExist() {
    // given
    EmailDto emailDto = new EmailDto();
    when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());

    // when
    assertThrows(UserNotFoundException.class, () -> resetPasswordService.resetPassword(emailDto));

    // then
    verify(resetPasswordTokenRepository, times(0)).save(any());
    verify(emailSenderService, times(0)).sendEmail(any(), any());
  }


  @Test
  void shouldThrowExceptionIfAccountExistsButIsNotVerified() {
    // given
    UserAccount userAccount = new UserAccount();
    userAccount.setEmail(TEST_EMAIL);
    userAccount.setEmailVerified(false);
    EmailDto emailDto = new EmailDto();
    emailDto.setEmail(TEST_EMAIL);
    when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());

    // when
    assertThrows(UserNotFoundException.class, () -> resetPasswordService.resetPassword(emailDto));

    // then
    verify(resetPasswordTokenRepository, times(0)).save(any());
    verify(emailSenderService, times(0)).sendEmail(any(), any());
  }

  @Test
  void shouldGetUserDetailsByPasswordResetToken() {
    // given
    ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
    resetPasswordToken.setResetPasswordToken("resetTokenId");
    resetPasswordToken.setUserAccountId("userAccountId");
    resetPasswordToken.setCreatedDate(Date.from(Instant.now().minus(4, ChronoUnit.HOURS)));
    when(resetPasswordTokenRepository.findByResetPasswordToken(resetPasswordToken.getResetPasswordToken()))
      .thenReturn(resetPasswordToken);
    UserAccount userAccount = new UserAccount();
    userAccount.setAccountId("userAccountId");
    userAccount.setEmail("testEmail");
    when(userRepository.findByAccountId(resetPasswordToken.getUserAccountId())).thenReturn(userAccount);

    // when
    ResetPasswordDto resetPasswordDto =
      resetPasswordService.getUserDetailsByResetPasswordToken(resetPasswordToken.getResetPasswordToken());

    // then
    assertThat(resetPasswordDto, notNullValue());
    assertThat(resetPasswordDto.getEmail(), is(userAccount.getEmail()));
  }

  @Test
  void shouldThrowExceptionIfTokenCantBeFound() {
    // given
    when(resetPasswordTokenRepository.findByResetPasswordToken(any())).thenReturn(null);

    // when
    assertThrows(UnauthorizedShoptasticException.class,
      () -> resetPasswordService.getUserDetailsByResetPasswordToken(any()));

    // then
    verify(userRepository, times(0)).findByAccountId(any());
  }

  @Test
  void shouldThrowExceptionIfTokenIsFoundButAlreadyUsed() {
    // given
    ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
    resetPasswordToken.setResetPasswordToken("resetTokenId");
    resetPasswordToken.setUserAccountId("userAccountId");
    resetPasswordToken.setUsed(true);
    when(resetPasswordTokenRepository.findByResetPasswordToken(any())).thenReturn(resetPasswordToken);

    // when
    assertThrows(UnauthorizedShoptasticException.class,
      () -> resetPasswordService.getUserDetailsByResetPasswordToken(any()));

    // then
    verify(userRepository, times(0)).findByAccountId(any());
  }

  @Test
  void shouldThrowExceptionIfTokenIsFoundButIsExpired() {
    // given
    ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
    resetPasswordToken.setResetPasswordToken("expiredTokenId");
    resetPasswordToken.setUserAccountId("userAccountId");
    resetPasswordToken.setCreatedDate(Date.from(Instant.now().minus(9, ChronoUnit.HOURS)));
    when(resetPasswordTokenRepository.findByResetPasswordToken(any())).thenReturn(resetPasswordToken);

    // when
    assertThrows(UnauthorizedShoptasticException.class,
      () -> resetPasswordService.getUserDetailsByResetPasswordToken(any()));

    // then
    verify(userRepository, times(0)).findByAccountId(any());
  }

  @Test
  void shouldUpdateThePasswordAndLoginTheUser() {
    // given
    ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
    resetPasswordToken.setResetPasswordToken("resetTokenId");
    resetPasswordToken.setUserAccountId("userAccountId");
    ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
    resetPasswordDto.setEmail("testEmail");
    resetPasswordDto.setNewPassword("newPassword");
    when(resetPasswordTokenRepository.findByResetPasswordToken(resetPasswordToken.getResetPasswordToken()))
      .thenReturn(resetPasswordToken);
    UserAccount userAccount = new UserAccount();
    userAccount.setAccountId(resetPasswordToken.getUserAccountId());
    userAccount.setPassword("testPassword");
    when(userRepository.findByAccountId(resetPasswordToken.getUserAccountId())).thenReturn(userAccount);
    OAuth oAuth = new OAuth();
    oAuth.setTokenExpirationMsec(1000L);
    when(authConfiguration.getOAuth()).thenReturn(oAuth);
    when(passwordEncoder.encode(resetPasswordDto.getNewPassword())).thenReturn("hashedNewPassword");
    when(userConverter.convertToUserPrincipal(userAccount)).thenReturn(new UserPrincipal());

    // when
    resetPasswordService.updatePassword(request, response, resetPasswordToken.getResetPasswordToken(),
      resetPasswordDto);

    // then
    verify(resetPasswordTokenRepository, times(1)).save(any());
    ArgumentCaptor<UserAccount> userAccountArgumentCaptor = ArgumentCaptor.forClass(UserAccount.class);
    verify(tokenProvider, times(1)).createToken(any());
    verify(userRepository).save(userAccountArgumentCaptor.capture());
    UserAccount updatedUserAccount = userAccountArgumentCaptor.getValue();
    assertThat(updatedUserAccount, notNullValue());
    assertThat(updatedUserAccount.getPassword(), is("hashedNewPassword"));
  }

  @Test
  void shouldThrowExceptionIfResetPasswordTokenCantBeFound() {
    // given
    ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
    when(resetPasswordTokenRepository.findByResetPasswordToken(any())).thenReturn(null);

    // when
    assertThrows(UnauthorizedShoptasticException.class, () -> resetPasswordService.updatePassword(request, response,
      "unknownToken", resetPasswordDto));

    // then
  }

  @Test
  void shouldThrowExceptionIfResetPasswordTokenCanBeFoundButIsAlreadyUsed() {
    // given
    ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
    ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
    resetPasswordToken.setResetPasswordToken("resetTokenId");
    resetPasswordToken.setUserAccountId("userAccountId");
    resetPasswordToken.setUsed(true);
    when(resetPasswordTokenRepository.findByResetPasswordToken(any())).thenReturn(resetPasswordToken);

    // when
    assertThrows(UnauthorizedShoptasticException.class, () -> resetPasswordService.updatePassword(request, response,
      "unknownToken", resetPasswordDto));

    // then
  }
}