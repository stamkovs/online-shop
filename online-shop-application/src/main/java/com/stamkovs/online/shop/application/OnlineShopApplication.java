package com.stamkovs.online.shop.application;

import com.stamkovs.online.shop.flyway.config.JdbcConnectionConfiguration;
import com.stamkovs.online.shop.rest.OnlineShopRestComponents;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starter class for the online shop.
 */
@SpringBootApplication(scanBasePackageClasses = {JdbcConnectionConfiguration.class, OnlineShopRestComponents.class})
public class OnlineShopApplication {

  public static void main(String[] args) {
    SpringApplication.run(OnlineShopApplication.class, args);
  }

}
