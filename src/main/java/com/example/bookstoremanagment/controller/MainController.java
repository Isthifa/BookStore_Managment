package com.example.bookstoremanagment.controller;

import com.example.bookstoremanagment.dto.AuthRequest;
import com.example.bookstoremanagment.dto.RoleDTO;
import com.example.bookstoremanagment.dto.UserDTO;
import com.example.bookstoremanagment.entity.Role;
import com.example.bookstoremanagment.entity.UserEntity;
import com.example.bookstoremanagment.service.JwtService;
import com.example.bookstoremanagment.service.Services;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {
    private final Services services;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/save")
    public ResponseEntity<UserEntity> save(@RequestBody @Valid UserDTO userDTO)
    {
        UserEntity userEntity = services.save(userDTO);
        return ResponseEntity.ok(userEntity);
    }

    @PostMapping("/AddRoles")
    public ResponseEntity<Role> addRoles(@RequestBody @Valid RoleDTO RoleDTO)
    {
        Role role=services.addRole(RoleDTO);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return "token: " + jwtService.genrateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }
}
