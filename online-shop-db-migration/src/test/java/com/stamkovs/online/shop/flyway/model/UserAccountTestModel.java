package com.stamkovs.online.shop.flyway.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Resource entity.
 */
@Getter
@Setter
public class UserAccountTestModel {

  private Integer id;
  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private int age;
}
