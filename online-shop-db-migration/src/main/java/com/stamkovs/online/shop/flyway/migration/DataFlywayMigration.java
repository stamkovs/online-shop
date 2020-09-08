package com.stamkovs.online.shop.flyway.migration;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
@Setter
public class DataFlywayMigration {

  private String dataFolder = "com/stamkovs/online/shop/flyway/migration/data/mssql";
  private String repeatableFolder = dataFolder + "repeatable";
  private String versionTable = "shop_migration_data_version";

  private final DataSource dataSource;

  @Value("${db.flyway.initMethod}")
  @Setter
  private String dbFlywayInitMethod;

  /**
   * Migrate to the latest Shop initial data.
   */
  public void migrate() {
    Flyway flyway = Flyway.configure()
      .table(versionTable)
      .dataSource(dataSource)
      .baselineOnMigrate(true)
      .locations(dataFolder, repeatableFolder)
      .load();
    if ("clean-migrate".equals(dbFlywayInitMethod)) {
      flyway.clean();
    }
    flyway.migrate();
  }
}
