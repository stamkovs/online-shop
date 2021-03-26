package com.stamkovs.online.shop.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum for the user roles.
 */
@Getter
@AllArgsConstructor
public enum UserRole {

  ADMIN(1),

  MANAGER(2),

  USER(3);

  private final int code;

  public static UserRole getByCode(int code) {
    for(UserRole userRole : values()) {
      if(userRole.getCode()== code) {
        return userRole;
      }
    }
    return USER;
  }
}
