package com.example.bookstoremanagment.dto;

import com.example.bookstoremanagment.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDTO {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role.RoleType roleName;
}
