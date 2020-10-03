package com.stamkovs.online.shop.flyway.migration;

import com.stamkovs.online.shop.flyway.config.JdbcConnectionConfiguration;
import com.stamkovs.online.shop.flyway.model.UserAccount;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@TestPropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = {JdbcConnectionConfiguration.class})
@EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@EnableConfigurationProperties
@SpringBootTest()
@Slf4j
class DataFlywayMigrationTest {

  public static final String CLEAN_MIGRATE = "clean-migrate";
  public static final String MIGRATE = "migrate";

  @InjectMocks
  DataFlywayMigration dataFlywayMigration;

  private Connection connection;

  private DataSource dataSource;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private String selectSQL = "SELECT * FROM user_account";

  private String h2UserEmail = "h2user@gmail.com";

  @Value("${db.flyway.dataFolder}")
  private String testDataFolder;

  @SneakyThrows
  @BeforeEach
  void setup() {
    dataSource = jdbcTemplate.getDataSource();
    if (dataSource != null) {
      connection = dataSource.getConnection();
    }
    dataFlywayMigration = new DataFlywayMigration(dataSource);
    dataFlywayMigration.setDataFolder(testDataFolder);
    dataFlywayMigration.setRepeatableFolder(testDataFolder + "/repeatable");
    dataFlywayMigration.setVersionTable("shop_h2_migration_data_version");
  }

  @Test
  void testInitialFlywayMigrate() {
    // given
    dataFlywayMigration.setDbFlywayInitMethod(MIGRATE);

    // when
    dataFlywayMigration.migrate();

    // then
    List<UserAccount> users = jdbcTemplate.query(selectSQL, new BeanPropertyRowMapper<>(UserAccount.class));
    assertThat(users.size(), is(1));

    // values should correspond to the ones defined in the h2 migration script
    assertThat(users.get(0).getEmail(), is(h2UserEmail));
    assertThat(users.get(0).getPassword(), is("h2Password"));
    assertThat(users.get(0).getFirstName(), is("h2"));
    assertThat(users.get(0).getLastName(), is("InMemory"));
    assertThat(users.get(0).getAge(), is(2));
  }

  @SneakyThrows
  @Test
  void shouldMigrateTheChangedUserDefinedInTheRepeatableScript() {
    // given
    dataFlywayMigration.setDbFlywayInitMethod(MIGRATE);

    // when
    dataFlywayMigration.migrate();
    Statement statement = connection.createStatement();
    String updateSql = "update user_account set email='changed@gmail.com'";
    // statement is auto-closable so it wont cause a lock on the db
    statement.executeUpdate(updateSql);
    List<UserAccount> users = jdbcTemplate.query(selectSQL, new BeanPropertyRowMapper<>(UserAccount.class));
    assertThat(users.size(), is(1));
    assertThat(users.get(0).getEmail(), is("changed@gmail.com"));

    // needed delay to finish update on h2 db before continuing with another migration
    Thread.sleep(1000);
    // then
    dataFlywayMigration.migrate();
    users = jdbcTemplate.query(selectSQL, new BeanPropertyRowMapper<>(UserAccount.class));
    assertThat(users.size(), is(1));
    assertThat(users.get(0).getEmail(), is(h2UserEmail));
  }

  @Test
  @SneakyThrows
  void shouldNotRemoveExistingDataWithFlywayMigrate() {
    // given
    dataFlywayMigration.setDbFlywayInitMethod(MIGRATE);

    // when
    // create the needed schema table and populate the table from the migration scripts
    dataFlywayMigration.migrate();
    Statement statement = connection.createStatement();
    String insertSql1 = "insert into user_account (id, email, password, first_name, last_name, age) " +
      "values (2, 'h2SecondUser@gmail.com', 'h2Password', 'h2', 'InMemory', 2)";
    String insertSql2 = "insert into user_account (id, email, password, first_name, last_name, age) " +
      "values (3, 'h2ThirdUser@gmail.com', 'h2Password', 'h2', 'InMemory', 2)";
    // statement is auto-closable so it wont cause a lock on the db
    statement.executeUpdate(insertSql1);
    statement.executeUpdate(insertSql2);

    // migrating again should not remove the existing data
    dataFlywayMigration.migrate();

    // then
    List<UserAccount> users = jdbcTemplate.query(selectSQL, new BeanPropertyRowMapper<>(UserAccount.class));
    assertThat(users.size(), is(3));
    assertThat(users.get(0).getId(), is(1));
    assertThat(users.get(0).getEmail(), is(h2UserEmail));
    assertThat(users.get(1).getId(), is(2));
    assertThat(users.get(1).getEmail(), is("h2SecondUser@gmail.com"));
    assertThat(users.get(2).getId(), is(3));
    assertThat(users.get(2).getEmail(), is("h2ThirdUser@gmail.com"));
  }

  @Test
  void testFlywayInitialCleanMigrate() {
    // given
    dataFlywayMigration.setDbFlywayInitMethod(CLEAN_MIGRATE);

    // when
    dataFlywayMigration.migrate();

    // then
    List<UserAccount> users = jdbcTemplate.query(selectSQL, new BeanPropertyRowMapper<>(UserAccount.class));
    assertThat(users.size(), is(1));
    assertThat(users.get(0).getEmail(), is(h2UserEmail));
  }

  @Test
  @SneakyThrows
  void shouldClearTheDbToTheStateDefinedInTheVersioningScripts() {
    // given
    dataFlywayMigration.setDbFlywayInitMethod(CLEAN_MIGRATE);

    // when
    // clear the db and create the needed schema first
    dataFlywayMigration.migrate();
    String insertSql1 = "insert into user_account (id, email, password, first_name, last_name, age) " +
      "values (2, 'h2SecondUser@gmail.com', 'h2Password', 'h2', 'InMemory', 2)";
    String insertSql2 = "insert into user_account (id, email, password, first_name, last_name, age) " +
      "values (3, 'h2ThirdUser@gmail.com', 'h2Password', 'h2', 'InMemory', 2)";
    Statement statement = connection.createStatement();
    statement.executeUpdate(insertSql1);
    statement.executeUpdate(insertSql2);

    // then
    List<UserAccount> users = jdbcTemplate.query(selectSQL, new BeanPropertyRowMapper<>(UserAccount.class));
    assertThat(users.size(), is(3));
    assertThat(users.get(0).getEmail(), is(h2UserEmail));
    assertThat(users.get(1).getEmail(), is("h2SecondUser@gmail.com"));
    assertThat(users.get(2).getEmail(), is("h2ThirdUser@gmail.com"));

    // do clean migrate on the db, it should have only the users defined in the h2 migration scripts
    dataFlywayMigration.migrate();
    // then
    users = jdbcTemplate.query(selectSQL, new BeanPropertyRowMapper<>(UserAccount.class));
    assertThat(users.size(), is(1));
    assertThat(users.get(0).getId(), is(1));
    assertThat(users.get(0).getEmail(), is(h2UserEmail));
  }

}