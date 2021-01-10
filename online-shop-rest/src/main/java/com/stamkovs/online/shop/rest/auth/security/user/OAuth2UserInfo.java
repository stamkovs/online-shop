package com.stamkovs.online.shop.rest.auth.security.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Model for the user info from the OAuth response.
 */
@AllArgsConstructor
@Getter
@Setter
public abstract class OAuth2UserInfo {
  protected Map<String, Object> attributes;

  public abstract String getId();

  public abstract String getName();

  public abstract String getEmail();

}
