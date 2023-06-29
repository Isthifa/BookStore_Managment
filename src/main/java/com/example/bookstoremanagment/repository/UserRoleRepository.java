package com.example.bookstoremanagment.repository;

import com.example.bookstoremanagment.entity.Role;
import com.example.bookstoremanagment.entity.UserEntity;
import com.example.bookstoremanagment.entity.UserRoleXref;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRoleXref, UUID> {
    List<UserRoleXref> findByUserEntity(UserEntity userEntity);

    List<UserRoleXref> findByUserEntityId(UUID id);

    @Query("SELECT u from UserRoleXref u JOIN UserEntity e on e.id=u.userEntity.id where e.id=:userID and u.role.roleName=:ROLE_AUTHOR")
    Optional<UserRoleXref> findByUserEntityAndRole(UUID userID, Role.RoleType ROLE_AUTHOR);

    @Query("delete from UserRoleXref u where u.userEntity.id=:id")
    @Modifying
    void deleteByUserEntityId(UUID id);
}
