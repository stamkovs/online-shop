package com.stamkovs.online.shop.rest.auth.config;

import com.stamkovs.online.shop.rest.auth.model.OAuth;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class for the OAuth configuration properties
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AuthConfiguration {

  private OAuth oAuth;
}
