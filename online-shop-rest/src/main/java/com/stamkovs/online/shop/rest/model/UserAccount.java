package com.stamkovs.online.shop.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.stamkovs.online.shop.rest.auth.model.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Persistence entity model for the user account.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_account", uniqueConstraints = {
  @UniqueConstraint(columnNames = "email")
})
public class UserAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
    name ="UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "account_id", updatable = false, nullable = false)
  private String accountId;

  @Column(nullable = false)
  @NonNull
  private String email;

  @Column(nullable = false)
  private Boolean emailVerified = false;

  @Column
  @NonNull
  private String password;

  @NotNull
  @Enumerated(EnumType.STRING)
  private AuthProvider provider;

  private String providerId;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column
  private int userRoleId;
}
