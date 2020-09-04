package com.stamkovs.online.shop.flyway.service;

import com.stamkovs.online.shop.flyway.migration.DataFlywayMigration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Performs the shop initial data migration.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DbMigrationService implements DbMigration {

  private final DataFlywayMigration migration;

  @Override
  public void update() {
    log.info("=== Data migration - START ===");
    migration.migrate();
    log.info("=== Data migration - FINISH ===");
  }
}
