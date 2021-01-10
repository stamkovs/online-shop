package com.stamkovs.online.shop.rest.auth.security;

import com.stamkovs.online.shop.rest.model.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Representation for the authentication Spring security principal containing the details of the authenticated user.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPrincipal implements OAuth2User, UserDetails {
  private Long id;
  private String email;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
  private Map<String, Object> attributes;

  public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserPrincipal create(UserAccount userAccount) {
    List<GrantedAuthority> authorities = Collections.
      singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    return new UserPrincipal(
      userAccount.getId(),
      userAccount.getEmail(),
      userAccount.getPassword(),
      authorities
    );
  }

  public static UserPrincipal create(UserAccount userAccount, Map<String, Object> attributes) {
    UserPrincipal userPrincipal = UserPrincipal.create(userAccount);
    userPrincipal.setAttributes(attributes);
    return userPrincipal;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String getName() {
    return String.valueOf(id);
  }
}
