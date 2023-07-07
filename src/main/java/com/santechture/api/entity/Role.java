package com.santechture.api.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

  ADMIN("Admin"),
  USER("User");

  private String name;

   Role (String name){
      this.name = name;
  }

  @Override
  public String getAuthority() {
    return this.name;
  }
}
