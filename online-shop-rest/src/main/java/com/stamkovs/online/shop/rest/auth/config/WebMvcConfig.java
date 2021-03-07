package com.stamkovs.online.shop.rest.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Web Mvc configuration needed for cors and interceptors.
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private static final long MAX_AGE_SECS = 3600;

  @Lazy
  private final JwtInterceptor jwtInterceptor;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedOrigins("https://localhost", "https://shop.stamkov.com")
      .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
      .allowedHeaders("*")
      .allowCredentials(true)
      .maxAge(MAX_AGE_SECS);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
      .excludePathPatterns("/**/logout", "/auth/login/oauthEndpoints");
  }
}
