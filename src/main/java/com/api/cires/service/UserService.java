package com.api.cires.service;

import com.api.cires.entity.User;
import com.api.cires.mapper.UserMapper;
import com.api.cires.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private final Faker faker = new Faker();
    private final UserRepository userRepository;
    private final Random random = new Random();
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public ResponseEntity<byte[]> generateUsers(int count) throws IOException {
        List<User> users = new ArrayList<>();
        for(int i=0; i<count;i++){
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setBirthDate(faker.date().birthday());
            user.setCity(faker.address().city());
            user.setCountry(faker.country().countryCode2());
            user.setAvatar(faker.avatar().image());
            user.setCompany(faker.company().name());
            user.setJobPosition(faker.job().position());
            user.setMobile(faker.phoneNumber().cellPhone());
            user.setUsername(faker.name().username());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(faker.internet().password(6,10));
            user.setRole(random.nextBoolean() ? "admin" : "user");
            users.add(user);
        }
        byte[] json = objectMapper.writeValueAsBytes(users);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename("users.json").build());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(json,headers, HttpStatus.OK);

    }

    public ResponseEntity<Map<String, Object>> saveUsers(MultipartFile file) throws IOException {
        List<User> users = objectMapper.readValue(file.getInputStream(), new TypeReference<List<User>>() {});
        int totalOfUsers = users.size();
        int success = 0;
        int failed = 0;

        for (User user : users){
            boolean emailExists = userRepository.findByEmail(user.getEmail()).isPresent();
            boolean usernameExists = userRepository.findByUsername(user.getUsername()).isPresent();
            if(emailExists || usernameExists){
                failed++;
            }
            else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                success++;
            }
        }
        Map<String,Object> response = new HashMap<>();
        response.put("total",totalOfUsers);
        response.put("success",success);
        response.put("failed",failed);
        return ResponseEntity.ok(response);

    }

    public ResponseEntity<?> viewMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> viewProfile(String username) {
        String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User userInPath = userRepository.findByUsername(username).orElse(null);
        User authUser = userRepository.findByEmail(authEmail).orElse(null);


        if(authUser != null && userInPath != null){
            if(authUser.getRole().equalsIgnoreCase("ADMIN") || authUser.getUsername().equals(username)){
                return ResponseEntity.ok(userMapper.toUserDTO(userInPath));
            }
            return ResponseEntity.status(403).body("Access denied");
        }
        return ResponseEntity.notFound().build();
    }
}
