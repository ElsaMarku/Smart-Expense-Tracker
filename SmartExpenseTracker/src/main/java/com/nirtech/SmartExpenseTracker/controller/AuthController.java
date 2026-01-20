package com.nirtech.SmartExpenseTracker.controller;

import com.nirtech.SmartExpenseTracker.dto.UserDTO;
import com.nirtech.SmartExpenseTracker.dto.UserResponseDTO;
import com.nirtech.SmartExpenseTracker.entity.User;
import com.nirtech.SmartExpenseTracker.util.JwtUtil;
import com.nirtech.SmartExpenseTracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Makes it a REST API controller
@RequestMapping("/auth") // Base URL for all user/auth operations
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User user = authService.registerUser(userDTO.getUsername(), userDTO.getPassword());
        return ResponseEntity.status(201).body(new UserResponseDTO(user.getId(), user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@Valid @RequestBody UserDTO userDTO) {
        User user = authService.loginUser(userDTO.getUsername(), userDTO.getPassword());
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());


        UserResponseDTO response = new UserResponseDTO(user.getId(), user.getUsername());
        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}