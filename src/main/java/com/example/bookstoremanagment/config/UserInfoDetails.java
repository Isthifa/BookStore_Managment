package com.example.bookstoremanagment.config;

import com.example.bookstoremanagment.entity.UserEntity;
import com.example.bookstoremanagment.entity.UserRoleXref;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class UserInfoDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private UserRoleXref userRoleXref;

    private String username;
    private String password;
    List<GrantedAuthority> authorities;
    public UserInfoDetails(UserEntity userEntity, List<UserRoleXref> userRoleXrefs){
        username= userEntity.getUsername();
        password= userEntity.getPassword();
        authorities= userRoleXrefs.stream().map(userRoleXref -> new SimpleGrantedAuthority(userRoleXref.getRole().getRoleName().toString())).collect(Collectors.toList());

        System.out.println("authorities: "+authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;

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
