package com.api.cires.controller;

import com.api.cires.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@RequestMapping(value = "/api/users")
@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(
            path = "/generate",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> generateUsers(@RequestParam(defaultValue = "1") int count) throws IOException {
        return userService.generateUsers(count);
    }

    @PostMapping(
            path = "/batch",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> saveUsers(@RequestParam("file")MultipartFile file)throws IOException{
        return userService.saveUsers(file);
    }

    @GetMapping("/me")
    public ResponseEntity<?> viewMyProfile(){
        return userService.viewMyProfile();
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> viewProfile(@PathVariable("username") String authUsername){
        return userService.viewProfile(authUsername);
    }
}
