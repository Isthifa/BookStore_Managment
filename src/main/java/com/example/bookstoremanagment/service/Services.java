package com.example.bookstoremanagment.service;

import com.example.bookstoremanagment.dto.RoleDTO;
import com.example.bookstoremanagment.dto.UserDTO;
import com.example.bookstoremanagment.entity.Role;
import com.example.bookstoremanagment.entity.UserEntity;

public interface Services {

    UserEntity save(UserDTO userDTO);

    Role addRole(RoleDTO roleDTO);

}
