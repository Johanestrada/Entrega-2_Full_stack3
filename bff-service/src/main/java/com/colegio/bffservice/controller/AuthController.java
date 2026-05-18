package com.colegio.bffservice.controller;

import com.colegio.bffservice.model.AuthResponse;
import com.colegio.bffservice.model.LoginRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static final Map<String, String> USERS = Map.of(
            "admin", "admin123",
            "alumno", "password"
    );

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4173")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.info("POST /auth/login - intent login usuario={}", loginRequest.getUsername());

        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String expectedPassword = USERS.get(loginRequest.getUsername());
        if (expectedPassword == null || !expectedPassword.equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = Base64.getEncoder().encodeToString(
                (loginRequest.getUsername() + ":" + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8)
        );

        return ResponseEntity.ok(new AuthResponse(loginRequest.getUsername(), token));
    }
}
