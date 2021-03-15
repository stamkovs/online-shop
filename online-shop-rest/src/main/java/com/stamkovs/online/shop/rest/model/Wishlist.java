package com.stamkovs.online.shop.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Persistence entity model for the user account.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_wishlist")
@IdClass(WishlistIdentity.class)
public class Wishlist {

  @Id
  @NotNull
  @Column(name = "user_account_id")
  private Long userAccountId;

  @Id
  @NotNull
  @Column(name = "product_id")
  private Long productId;

  @Column(name = "added_at")
  private Date addedAt;

}
