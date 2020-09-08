package com.stamkovs.online.shop.flyway.migration.data.h2.repeatable;

import com.stamkovs.online.shop.flyway.migration.data.AlwaysExecutableMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;

public class R__01_RepeatableAddOrUpdateUserAccounts extends AlwaysExecutableMigration {

  @Override
  public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
    String sql = Files.readString(Paths.get(ClassLoader.getSystemResource
      ("com/stamkovs/online/shop/flyway/migration/data/h2/repeatable/R__01_RepeatableAddOrUpdateUserAccount.sql")
      .toURI()));
    jdbcTemplate.execute(sql);
  }
}
