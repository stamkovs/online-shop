package com.stamkovs.online.shop.rest.model;

/**
 * Constants used for the shop.
 */
public final class ShopConstants {

  private ShopConstants() {
    // private constructor
  }

  public static final String BASE_URL = "https://shop.stamkov.com:8100";
  public static final String GOOGLE = "google";
  public static final String FACEBOOK = "facebook";
  public static final String GOOGLE_AUTH_URL = "/oauth2/authorize/google?redirect_uri=";
  public static final String FACEBOOK_AUTH_URL = "/oauth2/authorize/facebook?redirect_uri=";
  public static final String OAUTH_REDIRECT_URI = "https://shop.stamkov.com/home";

  public static final String FORWARD_SLASH = "/";
  public static final String IS_USER_LOGGED_IN = "is_user_logged_in";
  public static final String IS_USER_ADMIN = "is_user_admin";
  public static final String TRUE = "true";
  public static final String AUTHORIZATION = "Authorization";
  public static final String EMPTY_STRING = "";
}
