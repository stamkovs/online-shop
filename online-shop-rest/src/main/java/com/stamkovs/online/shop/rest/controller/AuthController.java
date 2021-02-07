package com.stamkovs.online.shop.rest.controller;

import com.stamkovs.online.shop.rest.model.UserLoginDto;
import com.stamkovs.online.shop.rest.service.LoginService;
import com.stamkovs.online.shop.rest.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.stamkovs.online.shop.rest.model.ShopConstants.BASE_URL;
import static com.stamkovs.online.shop.rest.model.ShopConstants.FACEBOOK;
import static com.stamkovs.online.shop.rest.model.ShopConstants.FACEBOOK_AUTH_URL;
import static com.stamkovs.online.shop.rest.model.ShopConstants.GOOGLE;
import static com.stamkovs.online.shop.rest.model.ShopConstants.GOOGLE_AUTH_URL;
import static com.stamkovs.online.shop.rest.model.ShopConstants.OAUTH_REDIRECT_URI;

/**
 * Authentication controller for retrieving oauth endpoints, register, login and logout of users, and checking
 * if user is logged in or not.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final LoginService loginService;
  private final LogoutService logoutService;

  /**
   * Log in the user account.
   *
   * @param response {@link HttpServletResponse}.
   * @param userLoginDto {@link UserLoginDto}.
   *
   * @return {@link ResponseEntity}.
   */
  @PostMapping(value = "/login")
  public ResponseEntity<Object> loginUserAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody UserLoginDto userLoginDto) {
    loginService.loginUser(request, response, userLoginDto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * Get the OAuth endpoints.
   */
  @GetMapping("/login/oauthEndpoints")
  public Map<String, String> endpoints() {
    Map<String, String> oauthEndpoints = new HashMap<>();
    oauthEndpoints.put(GOOGLE, BASE_URL + GOOGLE_AUTH_URL + OAUTH_REDIRECT_URI);
    oauthEndpoints.put(FACEBOOK, BASE_URL + FACEBOOK_AUTH_URL + OAUTH_REDIRECT_URI);

    return oauthEndpoints;
  }

  /**
   * Check if user is logged in, logic in {@link com.stamkovs.online.shop.rest.auth.config.JwtInterceptor}.
   */
  @GetMapping("/isLoggedIn")
  public void isLoggedIn() {
//    needed to intercept requests to check if user is logged in or anonymous
  }

  /**
   * Logout the user account.
   *
   * @param request {@link HttpServletRequest}.
   * @param response {@link HttpServletResponse}.
   */
  @GetMapping("/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    logoutService.logoutUser(request, response);
  }

}
