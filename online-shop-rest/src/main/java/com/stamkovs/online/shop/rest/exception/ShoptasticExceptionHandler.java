package com.stamkovs.online.shop.rest.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * Global exception handler for all exceptions in the controllers.
 */
@ControllerAdvice
@Slf4j
public class ShoptasticExceptionHandler {

  /**
   * Handler for all jwt exceptions which response status should be {@link HttpStatus#UNAUTHORIZED}. Exceptions
   * covered are all {@link JwtException}
   *
   * @param e the exception object
   *
   * @return {@link ResponseEntity}.
   */
  @ExceptionHandler(JwtException.class)
  @ResponseStatus(UNAUTHORIZED)
  public ResponseEntity<?> handleJWTException(JwtException e) {
    log.error("{} ({})", e.getClass().getSimpleName(), e.getMessage(), e);
    return new ResponseEntity<>(UNAUTHORIZED);
  }

  /**
   * Handler for all application exceptions which response status should be {@link HttpStatus#UNAUTHORIZED}. Exceptions
   * covered are all {@link JwtException}
   *
   * @param e the exception object
   *
   * @return {@link ResponseEntity}.
   */
  @ExceptionHandler(UnauthorizedShoptasticException.class)
  @ResponseStatus(UNAUTHORIZED)
  public ResponseEntity<?> handleUnauthorizedException(UnauthorizedShoptasticException e) {
    log.error("{} ({})", e.getClass().getSimpleName(), e.getMessage(), e);
    return new ResponseEntity<>(UNAUTHORIZED);
  }

  /**
   * Handler for all user not found exceptions which response status should be {@link HttpStatus#BAD_REQUEST}. Exceptions
   * covered are all {@link UserNotFoundException}
   *
   * @param e the exception object
   *
   * @return {@link ResponseEntity}.
   */
  @ExceptionHandler({UserNotFoundException.class, UserAlreadyExistsException.class, InvalidEmailException.class})
  @ResponseStatus(BAD_REQUEST)
  public ResponseEntity<?> handleBadRequestException(RuntimeException e) {
    log.error("{} ({})", e.getClass().getSimpleName(), e.getMessage(), e);
    return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
  }
}
