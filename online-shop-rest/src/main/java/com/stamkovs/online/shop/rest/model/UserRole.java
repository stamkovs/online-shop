package com.stamkovs.online.shop.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum for the user roles.
 */
@Getter
@AllArgsConstructor
public enum UserRole {

  CUSTOMER(1),

  MANAGER(2),

  ADMIN(3);

  private final int code;
}
