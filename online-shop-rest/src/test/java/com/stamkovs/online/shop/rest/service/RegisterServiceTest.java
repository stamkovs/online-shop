package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.config.AuthConfiguration;
import com.stamkovs.online.shop.rest.auth.model.AuthProvider;
import com.stamkovs.online.shop.rest.auth.model.ConfirmationToken;
import com.stamkovs.online.shop.rest.auth.model.OAuth;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import com.stamkovs.online.shop.rest.auth.security.UserPrincipal;
import com.stamkovs.online.shop.rest.converter.UserConverter;
import com.stamkovs.online.shop.rest.exception.UnauthorizedRedirectException;
import com.stamkovs.online.shop.rest.exception.UserAlreadyExistsException;
import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.model.UserRegisterDto;
import com.stamkovs.online.shop.rest.repository.ConfirmationTokenRepository;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link RegisterService}.
 */
@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

  private static final String TEST_EMAIL = "testEmail";
  private static final String USER_ACCOUNT_ID = "userAccountId";
  @InjectMocks
  private RegisterService registerService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ConfirmationTokenRepository confirmationTokenRepository;

  @Mock
  private EmailSenderService emailSenderService;

  private final PasswordEncoder realPasswordEncoder = new BCryptPasswordEncoder();

  private final UserConverter realUserConverter = new UserConverter(realPasswordEncoder);

  @Mock
  private UserConverter mockedUserConverter;

  @Mock
  private PasswordEncoder mockedPasswordEncoder;

  @Mock
  private AuthConfiguration authConfiguration;

  @Mock
  private HttpServletResponse response;

  @Mock
  private HttpServletRequest request;

  @Mock
  private TokenProvider tokenProvider;

  private UserAccount userAccount;
  private UserRegisterDto userRegisterDto;

  @BeforeEach
  void setUp() {
    userRegisterDto = new UserRegisterDto();
    userRegisterDto.setFirstName("Stole");
    userRegisterDto.setLastName("Stamkov");
    userRegisterDto.setPassword("test1234");
    userRegisterDto.setEmail("testEmail");
    userAccount = realUserConverter.convertUserRegisterDtoToUserAccount(userRegisterDto);
  }

  @Test
  void shouldSuccessfullyRegisterNewUserAccount() {
    // given
    UserAccount userAccount = realUserConverter.convertUserRegisterDtoToUserAccount(userRegisterDto);
    when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());
    when(emailSenderService.constructAccountVerificationEmail(any(), any())).thenReturn(new SimpleMailMessage());
    when(mockedUserConverter.convertUserRegisterDtoToUserAccount(userRegisterDto)).thenReturn(userAccount);

    // when
    registerService.registerUser(userRegisterDto);

    // then
    verify(mockedUserConverter, times(1)).convertUserRegisterDtoToUserAccount(any());
    ArgumentCaptor<UserAccount> userAccountArgumentCaptor = ArgumentCaptor.forClass(UserAccount.class);
    verify(userRepository).save(userAccountArgumentCaptor.capture());
    UserAccount registeredUserAccount = userAccountArgumentCaptor.getValue();
    assertThat(registeredUserAccount, notNullValue());
    assertThat(registeredUserAccount.getEmail(), is("testEmail"));
    assertThat(registeredUserAccount.getAccountId(), notNullValue());
    assertThat(registeredUserAccount.getProvider(), is(AuthProvider.local));
    assertThat(registeredUserAccount.getPassword(), not(userRegisterDto.getPassword()));
    verify(confirmationTokenRepository, times(1)).save(any());
    ArgumentCaptor<UserAccount> newUserInEmailSender = ArgumentCaptor.forClass(UserAccount.class);
    verify(emailSenderService, times(1)).constructAccountVerificationEmail(newUserInEmailSender.capture(), any());
    UserAccount userAccountInEmailSender = newUserInEmailSender.getValue();
    assertEquals(registeredUserAccount, userAccountInEmailSender);
    verify(emailSenderService, times(1)).sendEmail(any());
  }

  @Test
  void shouldUpdateRegisterDetailsForUserAccountIfUserExistsButNotVerified() {
    // given
    userRegisterDto.setFirstName("Stojan");
    when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(userAccount));
    when(emailSenderService.constructAccountVerificationEmail(any(), any())).thenReturn(new SimpleMailMessage());
    when(mockedPasswordEncoder.encode(userRegisterDto.getPassword())).thenReturn("hashedPassword");
    // when
    registerService.registerUser(userRegisterDto);

    // then
    verify(mockedUserConverter, times(0)).convertUserRegisterDtoToUserAccount(any());
    ArgumentCaptor<UserAccount> userAccountArgumentCaptor = ArgumentCaptor.forClass(UserAccount.class);
    verify(userRepository).save(userAccountArgumentCaptor.capture());
    UserAccount registeredUserAccount = userAccountArgumentCaptor.getValue();
    assertThat(registeredUserAccount, notNullValue());
    assertThat(registeredUserAccount.getEmail(), is("testEmail"));
    assertThat(registeredUserAccount.getAccountId(), notNullValue());
    assertThat(registeredUserAccount.getProvider(), is(AuthProvider.local));
    assertThat(registeredUserAccount.getFirstName(), is("Stojan"));
    assertThat(registeredUserAccount.getPassword(), not(userRegisterDto.getPassword()));
    verify(confirmationTokenRepository, times(1)).save(any());
    ArgumentCaptor<UserAccount> newUserInEmailSender = ArgumentCaptor.forClass(UserAccount.class);
    verify(emailSenderService, times(1)).constructAccountVerificationEmail(newUserInEmailSender.capture(), any());
    UserAccount userAccountInEmailSender = newUserInEmailSender.getValue();
    assertEquals(registeredUserAccount, userAccountInEmailSender);
    verify(emailSenderService, times(1)).sendEmail(any());
  }

  @Test
  void shouldThrowExceptionThatUserAccountExistsAndIsVerified() {
    // given
    userAccount.setEmailVerified(true);
    when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(userAccount));

    // when
    assertThrows(UserAlreadyExistsException.class, () -> registerService.registerUser(userRegisterDto));

    // then
  }

  @Test
  void shouldGetUserDetailsByConfirmationTokenAndVerifyEmail() {
    // given
    ConfirmationToken token = new ConfirmationToken();
    token.setConfirmationToken("testToken");
    token.setUserAccountId(USER_ACCOUNT_ID);

    UserAccount userAccount = new UserAccount();
    userAccount.setAccountId(USER_ACCOUNT_ID);
    userAccount.setEmail(TEST_EMAIL);
    userAccount.setEmailVerified(false);
    when(confirmationTokenRepository.findByConfirmationToken(token.getConfirmationToken())).thenReturn(token);
    when(userRepository.findByAccountId(token.getUserAccountId())).thenReturn(userAccount);
    UserPrincipal userPrincipal = new UserPrincipal();
    Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
    userPrincipal.setAuthorities(authorities);
    when(mockedUserConverter.convertToUserPrincipal(any())).thenReturn(userPrincipal);
    OAuth oauth = new OAuth();
    oauth.setTokenExpirationMsec(1000L);
    when(authConfiguration.getOAuth()).thenReturn(oauth);

    // when
    UserRegisterDto userRegisterDto = registerService.getUserDetailsByConfirmationToken(request, response,
      token.getConfirmationToken());

    // then
    assertThat(userRegisterDto, notNullValue());
    assertThat(userRegisterDto.getEmail(), is(TEST_EMAIL));
    ArgumentCaptor<UserAccount> userAccountArgumentCaptor = ArgumentCaptor.forClass(UserAccount.class);
    verify(userRepository).save(userAccountArgumentCaptor.capture());
    UserAccount verifiedUserAccount = userAccountArgumentCaptor.getValue();
    assertThat(verifiedUserAccount.getEmailVerified(), is(true));
    assertThat(verifiedUserAccount.getEmail(), is(TEST_EMAIL));
    assertThat(verifiedUserAccount.getAccountId(), is(USER_ACCOUNT_ID));
    verify(confirmationTokenRepository, times(1)).save(any());
    verify(userRepository, times(1)).save(any());
  }

  @Test
  void shouldThrowUnauthorizedExceptionIfTokenCantBeFound() {
    // given
    when(confirmationTokenRepository.findByConfirmationToken(any())).thenReturn(null);

    // when
    assertThrows(UnauthorizedRedirectException.class, () ->
      registerService.getUserDetailsByConfirmationToken(request, response, "unknownToken"));

    // then
  }

  @Test
  void shouldThrowUnauthorizedExceptionIfTokenIsAlreadyUsed() {
    // given
    ConfirmationToken token = new ConfirmationToken();
    token.setUsed(true);
    when(confirmationTokenRepository.findByConfirmationToken(any())).thenReturn(token);

    // when
    assertThrows(UnauthorizedRedirectException.class, () ->
      registerService.getUserDetailsByConfirmationToken(request, response, "unknownToken"));

    // then
  }

  @Test
  void shouldThrowExceptionWhenUserAccountCantBeFound() {
    // given
    ConfirmationToken token = new ConfirmationToken();
    token.setConfirmationToken("testToken");
    token.setUserAccountId(USER_ACCOUNT_ID);
    when(confirmationTokenRepository.findByConfirmationToken(token.getConfirmationToken())).thenReturn(token);
    when(userRepository.findByAccountId(token.getUserAccountId())).thenReturn(null);

    // when
    assertThrows(UserNotFoundException.class, () ->
      registerService.getUserDetailsByConfirmationToken(request, response, token.getConfirmationToken()));
  }

}