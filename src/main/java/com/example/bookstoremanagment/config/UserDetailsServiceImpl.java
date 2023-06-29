package com.example.bookstoremanagment.config;

import com.example.bookstoremanagment.entity.UserEntity;
import com.example.bookstoremanagment.entity.UserRoleXref;
import com.example.bookstoremanagment.repository.UserRepository;
import com.example.bookstoremanagment.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if(!userEntity.isPresent()){
            throw new UsernameNotFoundException("User not found");
        }

        List<UserRoleXref> userRoleXref = userRoleRepository.findByUserEntityId(userEntity.get().getId());

        return new UserInfoDetails(userEntity.get(), userRoleXref);
    }
}
