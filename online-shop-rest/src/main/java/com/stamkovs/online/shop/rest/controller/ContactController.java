package com.stamkovs.online.shop.rest.controller;

import com.stamkovs.online.shop.rest.model.ContactSupportMailDto;
import com.stamkovs.online.shop.rest.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for contact page form
 */
@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
@Slf4j
public class ContactController {

  private final ContactService contactService;

  /**
   * Endpoint for sending email to contact support.
   *
   * @param contactSupportMailDto {@link ContactSupportMailDto}.
   */
  @PostMapping(value = "/send-contact-support-mail")
  public ResponseEntity<Object> sendContactSupportEmail(@RequestBody ContactSupportMailDto contactSupportMailDto) {
    contactService.sendContactSupportEmail(contactSupportMailDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
