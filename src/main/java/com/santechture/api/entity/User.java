package com.santechture.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User implements UserDetails {

    @GeneratedValue
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Basic
    @Column(name = "username", nullable = false, length = 150)
    private String username;


    @Column(name = "password",nullable = false)
    private String password;


    @Basic
    @Column(name = "email", nullable = false, length = 250)
    private String email;


    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String email,String password) {
        this.username = username;
        this.email = email;
        this.password=password;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("User"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
}
