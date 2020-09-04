package com.stamkovs.online.shop.flyway.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.RandomIVGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * Configuration for the jdbc connection.
 */
@Configuration
@EnableConfigurationProperties
@PropertySource("classpath:application-dev.properties")
public class JdbcConnectionConfiguration {

  private static final String ENCRYPTION_ALGORITHM = "PBEWithHMACSHA512AndAES_256";

  @Bean
  public DataSource dataSource(DataSourceProperties dataSourceProperties,
                               StandardPBEStringEncryptor standardPBEStringEncryptor) {

    String decryptedPassword = standardPBEStringEncryptor.decrypt(dataSourceProperties.getPassword());
    dataSourceProperties.setPassword(decryptedPassword);
    return dataSourceProperties.initializeDataSourceBuilder().build();
  }

  @Bean
  public StandardPBEStringEncryptor standardPBEStringEncryptor(@Value("${property.encryption.key}") String encryptionKey) {
    StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
    standardPBEStringEncryptor.setAlgorithm(ENCRYPTION_ALGORITHM);
    standardPBEStringEncryptor.setIVGenerator(new RandomIVGenerator());
    standardPBEStringEncryptor.setPassword(encryptionKey);
    return standardPBEStringEncryptor;
  }

  @Bean
  public DatabaseDriver databaseDriver(DataSourceProperties dataSourceProperties) {
    return DatabaseDriver.fromJdbcUrl(dataSourceProperties.getUrl());
  }
}
