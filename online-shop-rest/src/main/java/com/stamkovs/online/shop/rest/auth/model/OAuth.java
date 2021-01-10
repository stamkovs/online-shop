package com.stamkovs.online.shop.rest.auth.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the OAuth configuration properties set with
 * {@link com.stamkovs.online.shop.rest.auth.config.AuthConfiguration}
 */
@Getter
@Setter
public class OAuth {

  private String tokenSecret;
  private long tokenExpirationMsec;
  private List<String> authorizedRedirectUris = new ArrayList<>();
}
