package com.stamkovs.online.shop.rest.auth.security.user;

import java.util.Map;

/**
 * Model for the user's google oauth info.
 */
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

  public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    return (String) attributes.get("sub");
  }

  @Override
  public String getName() {
    return (String) attributes.get("name");
  }

  @Override
  public String getFirstName() {
    return (String) attributes.get("first_name");
  }

  @Override
  public String getLastName() {
    return (String) attributes.get("last_name");
  }

  @Override
  public String getEmail() {
    return (String) attributes.get("email");
  }

}
