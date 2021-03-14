package com.stamkovs.online.shop.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for the contact support email.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactSupportMailDto {

  private String name;
  private String email;
  private String message;
}
