package com.stamkovs.online.shop.rest.repository;

import com.stamkovs.online.shop.rest.auth.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Custom repository for the confirmation token.
 */
@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {

  /**
   * Finds the confirmation token by the confirmation token string value.
   *
   * @param confirmationToken the confirmation token value.
   *
   * @return {@link ConfirmationToken}.
   */
  ConfirmationToken findByConfirmationToken(String confirmationToken);

  /**
   * Returns all {@link ConfirmationToken} in a list.
   */
  List<ConfirmationToken> findAll();

}
