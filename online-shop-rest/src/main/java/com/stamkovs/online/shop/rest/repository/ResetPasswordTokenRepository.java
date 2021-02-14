package com.stamkovs.online.shop.rest.repository;

import com.stamkovs.online.shop.rest.auth.model.ResetPasswordToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Custom repository for the confirmation token.
 */
@Repository
public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, Long> {

  ResetPasswordToken findByResetPasswordToken(String resetPasswordToken);

  List<ResetPasswordToken> findAll();
}
