package com.example.bookstoremanagment.service;

import com.example.bookstoremanagment.dto.RoleDTO;
import com.example.bookstoremanagment.dto.UserDTO;
import com.example.bookstoremanagment.dto.UserRoleDTO;
import com.example.bookstoremanagment.entity.Role;
import com.example.bookstoremanagment.entity.UserEntity;
import com.example.bookstoremanagment.entity.UserRole;
import com.example.bookstoremanagment.repository.RoleRepository;
import com.example.bookstoremanagment.repository.UserRepository;
import com.example.bookstoremanagment.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ServicesImpl implements Services{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserEntity save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        //saving the roles while registering
//        UserRoleDTO userRoleDTO=new UserRoleDTO();
//        Role.RoleType role=userDTO.getRoleName();
//        Role role1=roleRepository.findByName(role);
//        if(role1==null)
//        {
//            throw new RuntimeException("Role not found");
//        }else {
//            userRoleDTO.setRole(role1);
//            userRoleDTO.setUserEntity(userEntity);
//            UserRole userRole=modelMapper.map(userRoleDTO,UserRole.class);
//            userRoleRepository.save(userRole);
//        }
        return userRepository.save(userEntity);
    }

    @Override
    public Role addRole(RoleDTO roleDTO) {
        Role role=modelMapper.map(roleDTO,Role.class);
        return roleRepository.save(role);
    }
}
