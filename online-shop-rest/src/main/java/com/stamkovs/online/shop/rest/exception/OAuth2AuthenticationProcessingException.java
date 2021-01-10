package com.stamkovs.online.shop.rest.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception thrown during OAuth authentication processing.
 */
public class OAuth2AuthenticationProcessingException extends AuthenticationException {

  public OAuth2AuthenticationProcessingException(String msg) {
    super(msg);
  }
}
