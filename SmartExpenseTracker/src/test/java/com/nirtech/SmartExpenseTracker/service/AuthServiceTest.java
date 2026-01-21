package com.nirtech.SmartExpenseTracker.service;

import com.nirtech.SmartExpenseTracker.entity.User;
import com.nirtech.SmartExpenseTracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthService authService = new AuthService(userRepository, passwordEncoder);

    @Test
    void testRegisterUser_Success() {
        String username = "Alice";
        String rawPassword = "securePassword";

        User savedUser = new User();
        savedUser.setUsername(username);
        savedUser.setPassword("encodedPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // ✅ Call the actual method in your AuthService
        User result = authService.registerUser(username, rawPassword);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        String username = "Alice";
        String rawPassword = "securePassword";

        User existing = new User();
        existing.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existing));

        Exception exception = assertThrows(RuntimeException.class,
                () -> authService.registerUser(username, rawPassword));

        assertEquals("User already exists", exception.getMessage());
    }
}
