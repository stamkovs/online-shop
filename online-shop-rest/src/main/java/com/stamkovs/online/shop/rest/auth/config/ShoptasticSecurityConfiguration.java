package com.stamkovs.online.shop.rest.auth.config;

import com.stamkovs.online.shop.rest.auth.security.*;
import com.stamkovs.online.shop.rest.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *  Shoptastic security configuration.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
  securedEnabled = true,
  jsr250Enabled = true,
  prePostEnabled = true
)
@EnableJpaRepositories("com.stamkovs.online.shop.rest.repository")
@EntityScan(basePackageClasses = UserAccount.class)
@RequiredArgsConstructor
public class ShoptasticSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final CustomUserDetailsService customUserDetailsService;

  private final CustomOAuth2UserService customOAuth2UserService;

  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  /*
    By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
    the authorization request. But, since our service is stateless, we can't save it in
    the session. We'll save the request in a Base64 encoded cookie instead.
  */
  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

//  @Bean
//  public CorsConfigurationSource corsConfigurationSource() {
//    CorsConfiguration configuration = new CorsConfiguration();
//    configuration.setAllowedOrigins(Arrays.asList("*"));
//    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//    configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
//    configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", configuration);
//    return source;
//  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder
      .userDetailsService(customUserDetailsService)
      .passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .cors()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .csrf()
      .disable()
      .formLogin()
      .disable()
      .httpBasic()
      .disable()
      .exceptionHandling()
      .authenticationEntryPoint(new RestAuthenticationEntryPoint())
      .and()
      .authorizeRequests()
      .antMatchers("/",
        "/error",
        "/favicon.ico",
        "/**/*.png",
        "/**/*.gif",
        "/**/*.svg",
        "/**/*.jpg",
        "/**/*.html",
        "/**/*.css",
        "/**/*.js")
      .permitAll()
      .antMatchers("/auth/**", "/oauth2/**", "/api/**", "/rest/**")
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .oauth2Login()
      .authorizationEndpoint()
      .baseUri("/oauth2/authorize")
      .authorizationRequestRepository(cookieAuthorizationRequestRepository())
      .and()
      .redirectionEndpoint()
      .baseUri("/oauth2/callback/*")
      .and()
      .tokenEndpoint()
      .and()
      .userInfoEndpoint()
      .userService(customOAuth2UserService)
      .and()
      .successHandler(oAuth2AuthenticationSuccessHandler)
      .failureHandler(oAuth2AuthenticationFailureHandler);
  }
}