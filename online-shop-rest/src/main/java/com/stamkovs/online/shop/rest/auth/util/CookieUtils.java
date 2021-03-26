package com.stamkovs.online.shop.rest.auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stamkovs.online.shop.rest.exception.UnauthorizedShoptasticException;
import com.stamkovs.online.shop.rest.model.LoggedInUserDto;
import com.stamkovs.online.shop.rest.model.UserAccount;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

import static com.stamkovs.online.shop.rest.model.ShopConstants.*;

/**
 * Utility class for various cookie operations.
 */
public class CookieUtils {

  public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();

    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          return Optional.of(cookie);
        }
      }
    }

    return Optional.empty();
  }

  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath(FORWARD_SLASH);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          cookie.setValue(EMPTY_STRING);
          cookie.setPath(FORWARD_SLASH);
          cookie.setMaxAge(0);
          response.addCookie(cookie);
        }
      }
    }
  }

  /**
   * Adds the authorization http only cookie,
   */
  public static void addAuthorizationCookies(HttpServletResponse response, String bearerToken,
                                             int tokenExpirationInSeconds, UserAccount userAccount) {

    ObjectMapper objectMapper = new ObjectMapper();
    CookieUtils.addCookie(response, AUTHORIZATION, bearerToken, tokenExpirationInSeconds);
    Cookie isUserLoggedIn = new Cookie(IS_USER_LOGGED_IN, "1");
    isUserLoggedIn.setPath(FORWARD_SLASH);
    isUserLoggedIn.setMaxAge(86000);
    response.addCookie(isUserLoggedIn);

    LoggedInUserDto loggedInUserDto = new LoggedInUserDto();
    loggedInUserDto.setEmail(userAccount.getEmail());
    loggedInUserDto.setFirstName(userAccount.getFirstName());
    loggedInUserDto.setLastName(userAccount.getLastName());
    String userDetailsJson;
    try {
      userDetailsJson = objectMapper.writeValueAsString(loggedInUserDto);
    } catch (JsonProcessingException e) {
      throw new UnauthorizedShoptasticException("Error when converting user details to json");
    }
    Cookie userDetailsCookie = new Cookie("user_auth_information", userDetailsJson);
    userDetailsCookie.setPath(FORWARD_SLASH);
    userDetailsCookie.setMaxAge(86900);
    response.addCookie(userDetailsCookie);
  }

  public static String serialize(Object object) {
    return Base64.getUrlEncoder()
      .encodeToString(SerializationUtils.serialize(object));
  }

  public static <T> T deserialize(Cookie cookie, Class<T> cls) {
    return cls.cast(SerializationUtils.deserialize(
      Base64.getUrlDecoder().decode(cookie.getValue())));
  }

  public static String getJwtFromRequest(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();

    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(AUTHORIZATION)) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}
