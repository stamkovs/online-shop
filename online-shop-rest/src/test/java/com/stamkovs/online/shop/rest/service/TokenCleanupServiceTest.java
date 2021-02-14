package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.model.ConfirmationToken;
import com.stamkovs.online.shop.rest.auth.model.ResetPasswordToken;
import com.stamkovs.online.shop.rest.repository.ConfirmationTokenRepository;
import com.stamkovs.online.shop.rest.repository.ResetPasswordTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link TokenCleanupService}.
 */
@ExtendWith(MockitoExtension.class)
class TokenCleanupServiceTest {

  @InjectMocks
  private TokenCleanupService tokenCleanupService;

  @Mock
  private ConfirmationTokenRepository confirmationTokenRepository;

  @Mock
  private ResetPasswordTokenRepository resetPasswordTokenRepository;

  @Test
  void shouldFindExpiredOrUsedConfirmationTokens() {
    // given
    ConfirmationToken validConfirmationToken = new ConfirmationToken();
    validConfirmationToken.setTokenId(1L);
    validConfirmationToken.setCreatedDate(Date.from(Instant.now().minus(2, ChronoUnit.HOURS)));
    ConfirmationToken expiredConfirmationToken = new ConfirmationToken();
    expiredConfirmationToken.setTokenId(2L);
    ConfirmationToken usedConfirmationToken = new ConfirmationToken();
    usedConfirmationToken.setTokenId(3L);
    usedConfirmationToken.setCreatedDate(Date.from(Instant.now().minus(1, ChronoUnit.HOURS)));
    usedConfirmationToken.setUsed(true);
    expiredConfirmationToken.setCreatedDate(Date.from(Instant.now().minus(25, ChronoUnit.HOURS)));
    List<ConfirmationToken> confirmationTokenList = Arrays.asList(validConfirmationToken, expiredConfirmationToken,
      usedConfirmationToken);
    when(confirmationTokenRepository.findAll()).thenReturn(confirmationTokenList);

    // when
    tokenCleanupService.cleanUpConfirmationTokens();

    // then
    ArgumentCaptor<Long> confirmationTokenArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    verify(confirmationTokenRepository, times(2)).deleteById(confirmationTokenArgumentCaptor.capture());
    List<Long> deletedTokensList = confirmationTokenArgumentCaptor.getAllValues();
    assertThat(deletedTokensList.isEmpty(), is(false));
    assertThat(deletedTokensList.size(), is(2));
    assertThat(deletedTokensList.get(0), is(expiredConfirmationToken.getTokenId()));
    assertThat(deletedTokensList.get(1), is(usedConfirmationToken.getTokenId()));
  }

  @Test
  void shouldFindExpiredOrUsedResetPasswordTokens() {
    // given
    ResetPasswordToken validResetPasswordToken = new ResetPasswordToken();
    validResetPasswordToken.setTokenId(1L);
    validResetPasswordToken.setCreatedDate(Date.from(Instant.now().minus(2, ChronoUnit.HOURS)));
    ResetPasswordToken expiredResetPasswordToken = new ResetPasswordToken();
    expiredResetPasswordToken.setTokenId(2L);
    ResetPasswordToken usedResetPasswordToken = new ResetPasswordToken();
    usedResetPasswordToken.setTokenId(3L);
    usedResetPasswordToken.setCreatedDate(Date.from(Instant.now().minus(1, ChronoUnit.HOURS)));
    usedResetPasswordToken.setUsed(true);
    expiredResetPasswordToken.setCreatedDate(Date.from(Instant.now().minus(10, ChronoUnit.HOURS)));
    List<ResetPasswordToken> resetPasswordTokenList = Arrays.asList(validResetPasswordToken, expiredResetPasswordToken,
      usedResetPasswordToken);
    when(resetPasswordTokenRepository.findAll()).thenReturn(resetPasswordTokenList);

    // when
    tokenCleanupService.cleanUpResetPasswordTokens();

    // then
    ArgumentCaptor<Long> resetPasswordTokenArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    verify(resetPasswordTokenRepository, times(2)).deleteById(resetPasswordTokenArgumentCaptor.capture());
    List<Long> deletedTokensList = resetPasswordTokenArgumentCaptor.getAllValues();
    assertThat(deletedTokensList.isEmpty(), is(false));
    assertThat(deletedTokensList.size(), is(2));
    assertThat(deletedTokensList.get(0), is(expiredResetPasswordToken.getTokenId()));
    assertThat(deletedTokensList.get(1), is(usedResetPasswordToken.getTokenId()));
  }
}