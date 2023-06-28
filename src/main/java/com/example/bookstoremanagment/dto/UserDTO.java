package com.example.bookstoremanagment.dto;

import com.example.bookstoremanagment.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @NotEmpty
    @Size(min=4,max=20,message = "name must be between 4 and 20 characters")
    private String username;
    @Email(message = "email must be valid")
    private String email;
    @NotBlank(message = "phone must not be blank,should contain at 10 digits")
    private String phone;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "password must contain at least 8 characters,one uppercase,one lowercase,one digit and one special character")
    private String password;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role.RoleType roleName;
}
