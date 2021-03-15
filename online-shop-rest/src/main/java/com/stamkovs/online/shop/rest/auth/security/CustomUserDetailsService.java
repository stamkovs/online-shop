package com.stamkovs.online.shop.rest.auth.security;

import com.stamkovs.online.shop.rest.exception.UnauthorizedShoptasticException;
import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Custom user details service needed to load the user.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {
    UserAccount user = userRepository.findByEmailIgnoreCase(email)
      .orElseThrow(() ->
        new UsernameNotFoundException("User not found with email : " + email)
      );

    return UserPrincipal.create(user);
  }

  public UserDetails loadUserById(Long id) throws UserNotFoundException {
    UserAccount user = userRepository.findById(id).orElseThrow(
      () -> new UserNotFoundException("User with id " + id + " cant be found."));

    return UserPrincipal.create(user);
  }

  public UserAccount loadUserAccountById(Long id) throws UserNotFoundException {
    return userRepository.findById(id).orElseThrow(
      () -> new UserNotFoundException("User with id " + id + " cant be found."));
  }

  public Long getUserAccountIdFromAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof AnonymousAuthenticationToken) {
      throw new UnauthorizedShoptasticException("Unknown user.");
    }
    String userEmail = authentication.getName();
    Optional<UserAccount> userAccountOptional = userRepository.findByEmailIgnoreCase(userEmail);
    if (userAccountOptional.isEmpty()) {
      throw new UserNotFoundException("User does not exist.");
    }
    return userAccountOptional.get().getId();
  }
}
