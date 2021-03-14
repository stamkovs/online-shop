package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.exception.InvalidEmailException;
import com.stamkovs.online.shop.rest.model.ContactSupportMailDto;
import com.stamkovs.online.shop.rest.model.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
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
  private static final String RESET_YOUR_PASSWORD = "Reset your password";
  private static final String CONTACT_SUPPORT = "Contact support";

  @Value("${spring.mail.username}")
  private String springMailUsername;

  private final JavaMailSender javaMailSender;

  @Async
  public void sendEmail(SimpleMailMessage simpleMailMessage, String userEmail) {
    isValidEmail(userEmail);
    javaMailSender.send(simpleMailMessage);
    log.info("Successfully sent email from {} to {} ", simpleMailMessage.getFrom(), simpleMailMessage.getTo());
  }

  public SimpleMailMessage constructAccountVerificationEmail(UserAccount newUserAccount, String confirmationToken) {
    String message = "Dear " + newUserAccount.getFirstName() + " " + newUserAccount.getLastName() + "\n\nTo " +
      "complete your account, please click the following link: "
      + "https://shop.stamkov.com/confirm-account?token=" + confirmationToken + "\n\n"
      + "The link will expire after 24 hours.\n\nThank you.";
    return constructSimpleMailMessage(springMailUsername, newUserAccount.getEmail(),
      COMPLETE_YOUR_SHOPTASTIC_REGISTRATION, message);
  }

  public SimpleMailMessage constructResetPasswordEmail(UserAccount userAccount, String resetPasswordToken) {
    String message = "Dear " + userAccount.getFirstName() + " " + userAccount.getLastName() + "\n\nTo " +
      "reset your password, please click the following link: "
      + "https://shop.stamkov.com/reset-password?token=" + resetPasswordToken + "\n\n"
      + "The link will expire after 8 hours.\n\nThank you.";
    return constructSimpleMailMessage(springMailUsername, userAccount.getEmail(), RESET_YOUR_PASSWORD, message);
  }

  public SimpleMailMessage constructContactSupportEmail(ContactSupportMailDto contactSupportMailDto) {
    return constructSimpleMailMessage(contactSupportMailDto.getEmail(), springMailUsername, CONTACT_SUPPORT,
      contactSupportMailDto.getMessage());
  }

  private SimpleMailMessage constructSimpleMailMessage(String setFrom, String setTo, String subject, String message) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(setFrom);
    mailMessage.setTo(setTo);
    mailMessage.setSubject(subject);
    mailMessage.setText("From: " + setFrom + "\n\n" + message);
    return mailMessage;
  }

  private void isValidEmail(String email) {
    EmailValidator validator = EmailValidator.getInstance();
     if (!validator.isValid(email)) {
       throw new InvalidEmailException("Email: [" + email + "] is not valid.");
     }
  }
}
