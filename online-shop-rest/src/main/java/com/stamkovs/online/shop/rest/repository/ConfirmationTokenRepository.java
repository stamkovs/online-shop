package com.stamkovs.online.shop.rest.repository;

import com.stamkovs.online.shop.rest.auth.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Custom repository for the confirmation token.
 */
@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {

  ConfirmationToken findByConfirmationToken(String confirmationToken);
}
