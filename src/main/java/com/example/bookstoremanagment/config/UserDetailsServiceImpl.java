package com.example.bookstoremanagment.config;

import com.example.bookstoremanagment.entity.UserEntity;
import com.example.bookstoremanagment.entity.UserRole;
import com.example.bookstoremanagment.repository.UserRepository;
import com.example.bookstoremanagment.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<UserRole> userRoles = userRoleRepository.findByUserEntity(userEntity);

        return new UserInfoDetails(userEntity, userRoles);
    }
}
