package com.stamkovs.online.shop.rest.converter;

import com.stamkovs.online.shop.rest.auth.model.AuthProvider;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.model.UserRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Converter for user accounts.
 */
@Component
@RequiredArgsConstructor
public class UserConverter {

  private final PasswordEncoder passwordEncoder;

  /**
   * Converts from {@link UserRegisterDto} to {@link UserAccount}.
   *
   * @param userRegisterDto the userRegister details.
   *
   * @return {@link UserAccount}.
   */
  public UserAccount convertUserRegisterDtoToUserAccount(UserRegisterDto userRegisterDto) {
    UserAccount userAccount = new UserAccount();
    userAccount.setEmail(userRegisterDto.getEmail());
    userAccount.setEmailVerified(false);
    userAccount.setFirstName(userRegisterDto.getFirstName());
    userAccount.setLastName(userRegisterDto.getLastName());
    userAccount.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
    userAccount.setProvider(AuthProvider.local);
    userAccount.setUserRoleId(1);
    userAccount.setAccountId(UUID.randomUUID().toString());

    return userAccount;
  }

}
