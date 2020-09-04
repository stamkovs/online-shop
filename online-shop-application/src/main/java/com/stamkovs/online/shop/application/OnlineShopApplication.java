package com.stamkovs.online.shop.application;

import com.stamkovs.online.shop.flyway.config.JdbcConnectionConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starter class for the online shop.
 */
@SpringBootApplication(scanBasePackageClasses = {JdbcConnectionConfiguration.class})
public class OnlineShopApplication {

  public static void main(String[] args) {
    System.setProperty("server.port", "8100");
    SpringApplication.run(OnlineShopApplication.class, args);
  }

}
