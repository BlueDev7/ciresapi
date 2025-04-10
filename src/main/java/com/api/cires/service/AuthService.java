package com.api.cires.service;

import com.api.cires.dto.AuthRequest;
import com.api.cires.entity.User;
import com.api.cires.repository.UserRepository;
import com.api.cires.security.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ResponseEntity<?> authenticate(AuthRequest request) {
        User user = userRepository.
                findByIdentifier(request.getUsername())
                .orElse(null);
        if(user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())){
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(Map.of("accessToken",token));
        }
        else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

    }
}
