package com.stamkovs.online.shop.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for logged in user details.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoggedInUserDto {

  private String email;
  private String firstName;
  private String lastName;
}
