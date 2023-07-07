package com.santechture.api.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleagteAuthProvider implements AuthenticationProvider {

  @Autowired
  @Qualifier("userAuthenticationProvider")
  @Lazy
  private AuthenticationProvider authenticationProvider;


  @Autowired
  @Qualifier("adminAuthenticationProvider")
  @Lazy
  private AuthenticationProvider adminAuthprovider;



  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    try {
      // Try to authenticate using the admin auth provider
      return adminAuthprovider.authenticate(authentication);
    } catch (AuthenticationException e) {
      // If the admin authentication provider fails, delegate to another authentication provider
      AuthenticationProvider delegateProvider = getDelegateProvider();
      if (delegateProvider != null) {
        return delegateProvider.authenticate(authentication);
      } else {
        throw e;
      }
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }

  private AuthenticationProvider getDelegateProvider() {
    return authenticationProvider;
  }
}