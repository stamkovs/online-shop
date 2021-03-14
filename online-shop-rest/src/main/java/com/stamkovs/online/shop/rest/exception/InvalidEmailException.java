package com.stamkovs.online.shop.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when user is not found.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEmailException extends RuntimeException {

  public InvalidEmailException(String message) {
    super(message);
  }

}
