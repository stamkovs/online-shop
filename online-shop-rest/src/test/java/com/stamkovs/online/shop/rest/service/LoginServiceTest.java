package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.config.AuthConfiguration;
import com.stamkovs.online.shop.rest.auth.model.OAuth;
import com.stamkovs.online.shop.rest.auth.security.TokenProvider;
import com.stamkovs.online.shop.rest.auth.security.UserPrincipal;
import com.stamkovs.online.shop.rest.converter.UserConverter;
import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.model.UserLoginDto;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link LoginService}.
 */
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

  @InjectMocks
  private LoginService loginService;

  @Mock
  private TokenProvider tokenProvider;

  @Mock
  private UserRepository userRepository;

  @Mock
  private HttpServletResponse response;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserConverter userConverter;

  @Mock
  private AuthConfiguration authConfiguration;

  @Test
  void shouldLoginUserAndCreateToken() {
    // given
    UserAccount userAccount = UserAccount.builder()
      .accountId("accountId")
      .email("testEmail")
      .password("testPassword")
      .build();
    UserLoginDto userLoginDto = new UserLoginDto();
    userLoginDto.setEmail("testEmail");
    userLoginDto.setPassword("testPassword");
    when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(userAccount));
    when(passwordEncoder.matches(any(), any())).thenReturn(true);
    when(userConverter.convertToUserPrincipal(any())).thenReturn(new UserPrincipal());
    OAuth oAuth = new OAuth();
    oAuth.setTokenExpirationMsec(1000L);
    when(authConfiguration.getOAuth()).thenReturn(oAuth);

    // when
    loginService.loginUser(response, userLoginDto);

    // then

  }

  @Test
  void shouldThrowExceptionIfUserIsNotFoundByEmail() {
    // given
    UserLoginDto userLoginDto = new UserLoginDto();
    when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.empty());

    // when
    assertThrows(UserNotFoundException.class, () -> loginService.loginUser(response, userLoginDto));

    // then
  }

  @Test
  void shouldThrowExceptionIfUserIsFoundByEmailButPasswordIsNotCorrect() {
    // given
    UserAccount userAccount = UserAccount.builder()
      .accountId("accountId")
      .email("testEmail")
      .password("testPassword")
      .build();
    UserLoginDto userLoginDto = new UserLoginDto();
    userLoginDto.setEmail("testEmail");
    userLoginDto.setPassword("incorrectPassword");
    when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(userAccount));
    when(passwordEncoder.matches(any(), any())).thenReturn(false);

    // when
    assertThrows(UserNotFoundException.class, () -> loginService.loginUser(response, userLoginDto));

    // then
  }

}