package com.stamkovs.online.shop.flyway.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.RandomIVGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class EncryptDbPasswordTest {

  private static final String ENCRYPTION_KEY = "your-encryption-key";
  // Set your plain db password here to encrypt it in the test below and the remove it from here
  private static final String DB_PASSWORD = "set-your-password";
  // Set your encrypted db password here to decrypt it in the 2nd test below and the remove it from here
  private static final String DB_PASSWORD_ENCRYPTED ="";
  
  private StandardPBEStringEncryptor encryptor;

  @BeforeEach
  public void setup() {
    encryptor = new StandardPBEStringEncryptor();
    encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
    encryptor.setIVGenerator(new RandomIVGenerator());
    encryptor.setPassword(ENCRYPTION_KEY);
  }

  @Disabled
  @Test
  public void encryptPassword() {
    // given
    // when
    log.info("Encrypted password is {}", encryptor.encrypt(DB_PASSWORD));
    // then
  }

  @Disabled
  @Test
  public void decryptPassword() {
    // given
    // when
    log.info("Decrypted password is {}", encryptor.decrypt(DB_PASSWORD_ENCRYPTED));
    // then
  }

}
