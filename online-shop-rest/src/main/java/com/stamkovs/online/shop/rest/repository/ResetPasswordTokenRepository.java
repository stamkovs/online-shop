package com.stamkovs.online.shop.rest.repository;

import com.stamkovs.online.shop.rest.auth.model.ResetPasswordToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Custom repository for the confirmation token.
 */
@Repository
public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, String> {

  ResetPasswordToken findByResetPasswordToken(String resetPasswordToken);
}
