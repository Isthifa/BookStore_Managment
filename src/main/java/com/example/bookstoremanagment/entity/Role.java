package com.example.bookstoremanagment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Role extends BaseModel implements Serializable {

    public enum RoleType{ROLE_USER,ROLE_AUTHOR,ROLE_ADMIN}

        @Enumerated(EnumType.STRING)
        @Column(nullable = false,unique = true)
        private RoleType roleName;
}
