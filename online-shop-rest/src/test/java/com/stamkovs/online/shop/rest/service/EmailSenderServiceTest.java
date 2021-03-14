package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.exception.InvalidEmailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link EmailSenderService}.
 */
@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

  @InjectMocks
  private EmailSenderService emailSenderService;

  @Mock
  private JavaMailSender javaMailSender;

  @Test
  void shouldSendEmailWhenEmailIsValid() {
    // given

    // when
    emailSenderService.sendEmail(new SimpleMailMessage(), "shoptasticmk@gmail.com");

    // then
    verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
  }

  @Test
  void shouldNotSendEmailWhenEmailIsInvalid() {
    // given

    // when
    assertThrows(InvalidEmailException.class, () -> {
      emailSenderService.sendEmail(new SimpleMailMessage(), "shoptasticmk@.gmail.com");
    });

    // then
    verify(javaMailSender, times(0)).send(any(SimpleMailMessage.class));
  }
}