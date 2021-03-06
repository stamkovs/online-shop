package com.stamkovs.online.shop.rest.auth.security;

import com.stamkovs.online.shop.rest.auth.model.AuthProvider;
import com.stamkovs.online.shop.rest.auth.security.user.FacebookOAuth2UserInfo;
import com.stamkovs.online.shop.rest.exception.OAuth2AuthenticationProcessingException;
import com.stamkovs.online.shop.rest.model.UserAccount;
import com.stamkovs.online.shop.rest.auth.security.user.OAuth2UserInfo;
import com.stamkovs.online.shop.rest.auth.security.user.OAuth2UserInfoFactory;
import com.stamkovs.online.shop.rest.model.UserRole;
import com.stamkovs.online.shop.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

/**
 * Custom OAuth service needed to load and process the user.
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

    try {
      return processOAuth2User(oAuth2UserRequest, oAuth2User);
    } catch (Exception ex) {
      // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    OAuth2UserInfo oAuth2UserInfo =
      OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(),
        oAuth2User.getAttributes());
    if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
    }

    Optional<UserAccount> userOptional = userRepository.findByEmailIgnoreCase(oAuth2UserInfo.getEmail());
    UserAccount user;
    if (userOptional.isPresent()) {
      user = userOptional.get();
      if (user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {

        user = updateExistingUser(user, oAuth2UserInfo);
      }
    } else {
      user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
    }

    return UserPrincipal.create(user, oAuth2User.getAttributes());
  }

  private UserAccount registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    UserAccount user = new UserAccount();

    user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
    user.setProviderId(oAuth2UserInfo.getId());
    if (oAuth2UserInfo instanceof FacebookOAuth2UserInfo) {
      user.setFirstName(oAuth2UserInfo.getFirstName());
      user.setLastName(oAuth2UserInfo.getLastName());
    } else {
      user.setFirstName(oAuth2UserInfo.getName().split(" ")[0]);
      user.setLastName(oAuth2UserInfo.getName().split(" ")[1]);
    }
    user.setEmail(oAuth2UserInfo.getEmail());
    user.setUserRoleId(UserRole.USER.getCode());
    user.setEmailVerified(true);
    user.setAccountId(UUID.randomUUID().toString());
    return userRepository.save(user);
  }

  private UserAccount updateExistingUser(UserAccount existingUser, OAuth2UserInfo oAuth2UserInfo) {
    existingUser.setFirstName(oAuth2UserInfo.getName());
    return userRepository.save(existingUser);
  }

}
