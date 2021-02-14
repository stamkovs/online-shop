package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.auth.model.ConfirmationToken;
import com.stamkovs.online.shop.rest.auth.model.ResetPasswordToken;
import com.stamkovs.online.shop.rest.repository.ConfirmationTokenRepository;
import com.stamkovs.online.shop.rest.repository.ResetPasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that periodically checks for expired or used tokens and removes them from the db.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupService {

  private final ConfirmationTokenRepository confirmationTokenRepository;
  private final ResetPasswordTokenRepository resetPasswordTokenRepository;

  /**
   * Periodically check for expired or used confirmation tokens and remove them if any.
   */
  @Scheduled(cron = "${shop.confirmation-token-cleanup.cron}")
  public void cleanUpConfirmationTokens() {
    Instant pastDateMinus24HoursFromNow = Instant.now().minus(24, ChronoUnit.HOURS);
    List<ConfirmationToken> confirmationTokenList = confirmationTokenRepository.findAll();
    List<ConfirmationToken> expiredOrUsedConfirmationTokens =
      confirmationTokenList.stream().filter(confirmationToken -> Boolean.TRUE.equals(confirmationToken.isUsed()) ||
        confirmationToken.getCreatedDate().toInstant().isBefore(pastDateMinus24HoursFromNow))
        .collect(Collectors.toList());

    for (ConfirmationToken confirmationToken: expiredOrUsedConfirmationTokens) {
      confirmationTokenRepository.deleteById(confirmationToken.getTokenId());
    }
    log.info("Deleted {} expired or used confirmation tokens.", expiredOrUsedConfirmationTokens.size());
  }

  /**
   * Periodically check for expired or used reset password tokens and remove them if any.
   */
  @Scheduled(cron = "${shop.reset-password-token-cleanup.cron}")
  public void cleanUpResetPasswordTokens() {
    Instant pastDateMinus8HoursFromNow = Instant.now().minus(8, ChronoUnit.HOURS);
    List<ResetPasswordToken> resetPasswordTokenList = resetPasswordTokenRepository.findAll();
    List<ResetPasswordToken> expiredOrUsedResetPasswordTokens =
      resetPasswordTokenList.stream().filter(resetPasswordToken -> Boolean.TRUE.equals(resetPasswordToken.isUsed()) ||
        resetPasswordToken.getCreatedDate().toInstant().isBefore(pastDateMinus8HoursFromNow))
        .collect(Collectors.toList());

    for (ResetPasswordToken resetPasswordToken: expiredOrUsedResetPasswordTokens) {
      resetPasswordTokenRepository.deleteById(resetPasswordToken.getTokenId());
    }
    log.info("Deleted {} expired or used password reset tokens.", expiredOrUsedResetPasswordTokens.size());
  }
}
