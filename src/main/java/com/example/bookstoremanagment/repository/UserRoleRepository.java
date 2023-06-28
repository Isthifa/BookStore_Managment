package com.example.bookstoremanagment.repository;

import com.example.bookstoremanagment.entity.UserEntity;
import com.example.bookstoremanagment.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    List<UserRole> findByUserEntity(UserEntity userEntity);
}
