package com.stamkovs.online.shop.rest.service;

import com.stamkovs.online.shop.rest.model.ContactSupportMailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Service for contact page.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ContactService {

  private final EmailSenderService emailSenderService;

  public void sendContactSupportEmail(ContactSupportMailDto contactSupportMailDto) {
    SimpleMailMessage mailMessage = emailSenderService.constructContactSupportEmail(contactSupportMailDto);
    emailSenderService.sendEmail(mailMessage, contactSupportMailDto.getEmail());
  }
}
