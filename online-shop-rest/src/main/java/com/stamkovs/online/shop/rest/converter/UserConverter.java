package com.stamkovs.online.shop.rest.converter;

import com.stamkovs.online.shop.rest.auth.model.AuthProvider;
import com.stamkovs.online.shop.rest.auth.security.UserPrincipal;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.model.UserRegisterDto;
import com.stamkovs.online.shop.rest.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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
    userAccount.setUserRoleId(3);
    userAccount.setAccountId(UUID.randomUUID().toString());

    return userAccount;
  }

  /**
   * Converts from {@link UserAccount} combined data to {@link UserPrincipal}.
   *
   * @param userAccount {@link UserAccount}.
   *
   * @return {@link UserPrincipal}.
   */
  public UserPrincipal convertToUserPrincipal(UserAccount userAccount, UserRole userRole) {
    UserPrincipal userPrincipal = new UserPrincipal();
    List<GrantedAuthority> authorities = Collections.
      singletonList(new SimpleGrantedAuthority(userRole.name()));
    userPrincipal.setAuthorities(authorities);
    userPrincipal.setEmail(userAccount.getEmail());
    userPrincipal.setId(userAccount.getId());
    userPrincipal.setPassword(userAccount.getPassword());
    userPrincipal.setUserAccountId(userAccount.getAccountId());
    return userPrincipal;
  }

}
