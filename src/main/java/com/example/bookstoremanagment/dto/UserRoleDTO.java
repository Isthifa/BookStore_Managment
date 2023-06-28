package com.example.bookstoremanagment.dto;

import com.example.bookstoremanagment.entity.Role;
import com.example.bookstoremanagment.entity.UserEntity;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleDTO {

    @Column(nullable = false)
    private UserEntity userEntity;
    @Column(nullable = false)
    private Role role;
}
