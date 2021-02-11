package com.stamkovs.online.shop.rest.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Persistence entity model for the confirmation token table.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reset_password_token")
public class ResetPasswordToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "token_id")
  private long tokenId;

  @Column(name = "reset_password_token")
  private String resetPasswordToken;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @Column(name = "user_account_id")
  private String userAccountId;

  @Column(name = "is_used")
  private boolean isUsed;

}