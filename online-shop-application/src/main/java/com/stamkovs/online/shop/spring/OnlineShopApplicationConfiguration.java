package com.stamkovs.online.shop.spring;

import javax.sql.DataSource;

import org.jasypt.salt.RandomIVGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the jdbc connection.
 */
@Configuration
@EnableConfigurationProperties
public class OnlineShopApplicationConfiguration {

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
}
