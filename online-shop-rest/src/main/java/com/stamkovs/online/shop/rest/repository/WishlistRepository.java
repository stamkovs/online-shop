package com.stamkovs.online.shop.rest.repository;

import com.stamkovs.online.shop.rest.model.Wishlist;
import com.stamkovs.online.shop.rest.model.WishlistIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Custom repository for the user.
 */
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, WishlistIdentity> {

  List<Wishlist> findAllByUserAccountId(Long user);

  Optional<Wishlist> findByUserAccountIdAndProductId(Long userAccountId, Long productId);

  void deleteByUserAccountIdAndProductId(Long userAccountId, Long productId);

  void deleteByUserAccountId(Long userAccountId);
}