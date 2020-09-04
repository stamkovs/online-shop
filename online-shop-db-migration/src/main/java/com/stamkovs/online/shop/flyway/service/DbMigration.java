package com.stamkovs.online.shop.flyway.service;

/**
 * Updates the DB to a defined state.
 */
public interface DbMigration {

  /**
   * Updates the DB to a defined state.
   */
  void update() throws Exception;

}
