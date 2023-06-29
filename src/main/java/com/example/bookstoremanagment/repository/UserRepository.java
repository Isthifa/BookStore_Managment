package com.example.bookstoremanagment.repository;

import com.example.bookstoremanagment.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

    void deleteByUsername(String username);
//    Optional<UserEntity> findByUserName(String username);
}
