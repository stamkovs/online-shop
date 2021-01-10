package com.stamkovs.online.shop.rest.auth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * When user tries to access protected resource without authentication.
 */
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {


  @Override
  public void commence(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AuthenticationException e) throws IOException {
    log.error("Responding with unauthorized error. Message - {}", e.getMessage());
    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
      e.getLocalizedMessage());
  }
}
