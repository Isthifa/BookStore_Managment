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
    public enum RoleType{USER,AUTHOR}

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private RoleType name;
}
