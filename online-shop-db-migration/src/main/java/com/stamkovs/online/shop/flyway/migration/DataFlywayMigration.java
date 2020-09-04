package com.stamkovs.online.shop.flyway.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Flyway migration for the Shop db data.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DataFlywayMigration {

  private static final String DATA_FOLDER = "com/stamkovs/online/shop/flyway/migration/data/";
  private static final String REPEATABLE_FOLDER = DATA_FOLDER + "repeatable";
  private static final String VERSION_TABLE = "shop_migration_data_version";

  private final DataSource dataSource;

  @Value("${db.flyway.initMethod}")
  private String dbFlywayInitMethod;

  /**
   * Migrate to the latest Shop initial data.
   */
  public void migrate() {
    Flyway flyway = Flyway.configure()
      .table(VERSION_TABLE)
      .dataSource(dataSource)
      .baselineOnMigrate(true)
      .locations(DATA_FOLDER, REPEATABLE_FOLDER)
      .load();
    if ("clean-migrate".equals(dbFlywayInitMethod)) {
      flyway.clean();
    }
    flyway.migrate();
  }
}
