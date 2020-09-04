package com.stamkovs.online.shop.flyway.migration.data.repeatable;

import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.stereotype.Service;

/**
 * Migration that will always be executed regardless if there were any changes made.
 * It's up to the migration logic to check if any change should be made on db.
 */
@Service
public abstract class AlwaysExecutableMigration implements SpringJdbcMigration, MigrationChecksumProvider {

  /**
   * Flyway compares the checksum with the previously executed one.
   * We always return timestamp with preceision in seconds (due to int limitation)
   * Even if overflaw happens (sometime, maxint=2147483647), the next value would be different than the previous one.
   * @return integer
   */
  @Override
  public Integer getChecksum() {
    long timeInMillis = System.currentTimeMillis();
    long timeInSeconds = timeInMillis / 1000;
    return (int) timeInSeconds;
  }
}
