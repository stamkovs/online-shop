package com.stamkovs.online.shop.rest.repository;

import com.stamkovs.online.shop.rest.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Custom repository for the user.
 */
@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {

  Optional<UserAccount> findByEmailIgnoreCase(String email);

  UserAccount findByAccountId(String accountId);

}