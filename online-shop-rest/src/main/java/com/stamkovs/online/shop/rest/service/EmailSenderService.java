package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.model.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

  private static final String COMPLETE_YOUR_SHOPTASTIC_REGISTRATION = "Complete your Shoptastic registration!";

  @Value("${spring.mail.username}")
  private String springMailUsername;

  private final JavaMailSender javaMailSender;

  @Async
  public void sendEmail(SimpleMailMessage email) {
    javaMailSender.send(email);
    log.info("Successfully sent email to {} ", (Object) email.getTo());
  }

  public SimpleMailMessage constructAccountVerificationEmail(UserAccount newUserAccount, String confirmationToken) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(newUserAccount.getEmail());
    mailMessage.setSubject(COMPLETE_YOUR_SHOPTASTIC_REGISTRATION);
    mailMessage.setFrom(springMailUsername);
    mailMessage.setText("Dear " + newUserAccount.getFirstName() + " " + newUserAccount.getLastName() + "\n\nTo " +
      "complete your " +
      "account, please click here : "
      + "http://localhost:4400/confirm-account?token=" + confirmationToken + "\n\n"
      + "The link will expire after 24 hours.\n\nThank you.");

    return mailMessage;
  }
}
