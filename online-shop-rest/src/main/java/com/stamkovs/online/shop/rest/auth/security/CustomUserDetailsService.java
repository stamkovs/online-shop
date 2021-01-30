package com.stamkovs.online.shop.rest.auth.security;

import com.stamkovs.online.shop.rest.exception.UserNotFoundException;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom user details service needed to load the user.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {
    UserAccount user = userRepository.findByEmailIgnoreCase(email)
      .orElseThrow(() ->
        new UsernameNotFoundException("User not found with email : " + email)
      );

    return UserPrincipal.create(user);
  }

  @Transactional
  public UserDetails loadUserById(Long id) {
    UserAccount user = userRepository.findById(id).orElseThrow(
      () -> new UserNotFoundException("User with id " + id + " cant be found."));

    return UserPrincipal.create(user);
  }

  @Transactional
  public UserAccount loadUserAccountById(Long id) throws UserNotFoundException {
    return userRepository.findById(id).orElseThrow(
      () -> new UserNotFoundException("User with id " + id + " cant be found."));
  }
}
