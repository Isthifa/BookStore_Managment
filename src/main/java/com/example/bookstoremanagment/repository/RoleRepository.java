package com.example.bookstoremanagment.repository;

import com.example.bookstoremanagment.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID>{
    Role findByName(Role.RoleType role);
}
