package com.stamkovs.online.shop.application;

import com.stamkovs.online.shop.flyway.config.JdbcConnectionConfiguration;
import com.stamkovs.online.shop.rest.OnlineShopRestComponents;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Starter class for the online shop.
 */
@SpringBootApplication(scanBasePackageClasses = {JdbcConnectionConfiguration.class, OnlineShopRestComponents.class})
public class OnlineShopApplication {

  public static void main(String[] args) {
    SpringApplication.run(OnlineShopApplication.class, args);
  }


  @Bean
  public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
    return (serverFactory) -> serverFactory.addContextCustomizers(
      (context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
  }

}
