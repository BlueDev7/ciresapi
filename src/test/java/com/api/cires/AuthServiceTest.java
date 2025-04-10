package com.api.cires;

import com.api.cires.dto.AuthRequest;
import com.api.cires.entity.User;
import com.api.cires.repository.UserRepository;
import com.api.cires.security.JWTUtil;
import com.api.cires.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate_ValidCredentials() {
        // Arrange
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("ahmed@cires.com");
        authRequest.setPassword("123456");

        User user = new User();
        user.setEmail("ahmed@cires.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByIdentifier("ahmed@cires.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("ahmed@cires.com")).thenReturn("mocked.jwt.token");

        // Act
        ResponseEntity<?> response = authService.authenticate(authRequest);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        Map<?,?> body = (Map<?, ?>) response.getBody();
        assertNotNull(body);
        assertEquals("mocked.jwt.token", body.get("accessToken"));
    }

}
