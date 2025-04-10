package com.api.cires;

import com.api.cires.entity.User;
import com.api.cires.mapper.UserMapper;
import com.api.cires.repository.UserRepository;
import com.api.cires.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(
                userRepository,
                new ObjectMapper(),
                passwordEncoder,
                userMapper
        );
    }

    @Test
    void testGenerateUsers() throws Exception {
        int count = 1;

        ResponseEntity<byte[]> response = userService.generateUsers(count);

        assertNotNull(response.getBody());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void testSaveUsers() throws Exception {

        User user1 = new User();
        user1.setFirstName("Ahmed");
        user1.setLastName("ZRIRAK");
        user1.setEmail("ahmed@cires.com");
        user1.setUsername("ahmed.zrirak");
        user1.setPassword("123456");
        user1.setRole("admin");

        List<User> userList = new ArrayList<>();
        userList.add(user1);

        byte[] jsonBytes = new ObjectMapper().writeValueAsBytes(userList);

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(jsonBytes));


        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());


        when(passwordEncoder.encode(anyString())).thenReturn("123456");


        ResponseEntity<Map<String, Object>> response = userService.saveUsers(mockFile);


        assertEquals(200, response.getStatusCode().value());

        Map<String, Object> result = response.getBody();
        assertNotNull(result);
        assertEquals(1, result.get("total"));
        assertEquals(1, result.get("success"));
        assertEquals(0, result.get("failed"));


        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testViewProfile_NonAdmin() {
        // Arrange
        String targetUsername = "otherUser";
        String userEmail = "user@example.com";

        User targetUser = new User();
        targetUser.setUsername(targetUsername);

        User regularUser = new User();
        regularUser.setEmail(userEmail);
        regularUser.setUsername("myself");
        regularUser.setRole("user");

        // Mock SecurityContext
        Authentication  auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(userEmail);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByUsername(targetUsername)).thenReturn(Optional.of(targetUser));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(regularUser));

        // Act
        ResponseEntity<?> response = userService.viewProfile(targetUsername);

        // Assert
        assertNotNull(response);
        assertEquals(403, response.getStatusCode().value());
        assertEquals("Access denied", response.getBody());
    }



}
