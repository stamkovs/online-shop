package com.stamkovs.online.shop.flyway.app;

import com.stamkovs.online.shop.flyway.DbMigrationComponents;
import com.stamkovs.online.shop.flyway.service.DbMigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

/**
 * Spring boot Shop DB updater application.
 */
@SpringBootApplication(scanBasePackageClasses = DbMigrationComponents.class, exclude = FlywayAutoConfiguration.class)
@Slf4j
@RequiredArgsConstructor
public class DbMigrationApplication implements CommandLineRunner {

  private final DbMigrationService dbUpdater;

  /**
   * The main method of the spring boot application.
   *
   * @param args the input arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(DbMigrationApplication.class, args);
  }

  @Override
  public void run(String... strings) {
    dbUpdater.update();
    log.info("The DB migration finished with the DB updates.");
    System.exit(0);
  }
}
