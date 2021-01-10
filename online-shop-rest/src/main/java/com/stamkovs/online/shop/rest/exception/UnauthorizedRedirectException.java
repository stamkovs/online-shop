package com.stamkovs.online.shop.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when oauth request cant be redirected.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnauthorizedRedirectException extends RuntimeException {

  public UnauthorizedRedirectException(String message) {
    super(message);
  }
}
